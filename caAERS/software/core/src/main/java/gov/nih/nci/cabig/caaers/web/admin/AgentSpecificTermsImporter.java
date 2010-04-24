package gov.nih.nci.cabig.caaers.web.admin;

import gov.nih.nci.cabig.caaers.CaaersSystemException;
import gov.nih.nci.cabig.caaers.dao.*;
import gov.nih.nci.cabig.caaers.domain.*;
import gov.nih.nci.cabig.caaers.domain.repository.TerminologyRepository;
import gov.nih.nci.cabig.caaers.service.AgentSpecificAdverseEventListService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* @author Ion C. Olaru
* 
* */
public class AgentSpecificTermsImporter {


	private POIFSFileSystem poifs;
	private HSSFWorkbook wb;
	private HSSFSheet sh;

	private AgentDao agentDao;
    private TerminologyRepository terminologyRepository;
    private AgentSpecificTermDao agentSpecificTermDao;
    private AgentSpecificAdverseEventListService asaelService;

    private int rowCount = 0;
    private int columnsCount = 0;
    private Map<String, Agent> agents = new HashMap<String, Agent>();
    private Map<String, String> asaelCache = new HashMap<String, String>();
    private int asael;
    
    private Map<String, Short> headers = new HashMap() {{
        put("NSC", -1);
        put("CTCAE_VERSION", -1);
        put("CTCAE_CATEGORY", -1);
        put("AE_TERM", -1);
    }};

    public static File file = new File("/home/dell/Desktop/asael.xls");

    public AgentSpecificTermsImporter() {
    }
    
    public AgentSpecificTermsImporter(File f) {
        file = f;
    }

    public static void main(String[] args) throws Exception {
        AgentSpecificTermsImporter im = new AgentSpecificTermsImporter(file);
        im.importFile();
    }

    public void importFile() throws Exception {
        bootstrap(file);

        // Loading ASAE list
        int i = 1;
        while (i <= rowCount) {

            HSSFRow row = sh.getRow(i);
            if (row != null) {
                String nsc = getCellData("", i, row.getCell((short)headers.get("NSC")));
                String ctcae_category= getCellData("", i, row.getCell((short)headers.get("CTCAE_CATEGORY")));
                int ctcae_version = Integer.parseInt(getCellData("", i, row.getCell((short)headers.get("CTCAE_VERSION"))));
                String ae_term = getCellData("", i, row.getCell((short)headers.get("AE_TERM")));

                // System.out.println(String.format("NSC:%s,   V:%s,   C:%s,   T:%s", nsc, ctcae_version, ctcae_category, ae_term));

                Agent a = agents.get(nsc);
                if (a == null) {
                    a = agentDao.getByNscNumber(nsc);
                    System.out.println("Found agent [" + a.getName() + "] for NSC: [" + nsc + "]");
                    agents.put(nsc, a);
                }
                
                if (a != null) {
                    AgentSpecificCtcTerm t = new AgentSpecificCtcTerm();
                    t.setAgent(a);

                    List<CtcTerm> list = terminologyRepository.getCtcTerm(ctcae_category, ctcae_version, ae_term);
                    if (list.size() == 0) {
                        System.out.println("Err. Term not found");
                    } else {
                        t.setCtcTerm(list.get(0));
                        persistASAE(t);
                        asael++;
                    }
                    
                } else {
                    System.out.println("Err. The agent was not found by its NSC: " + nsc);
                }

            }
            i++;
        }

        System.out.println();
        System.out.println(String.format("Loaded %d agents", agents.size()));
        System.out.println(String.format("Built %d ASAE terms", asael));
        System.out.println("OK.");
    }

    private boolean isAgentSpecificTermPersisted(AgentSpecificTerm at) {
        List<AgentSpecificCtcTerm> l = agentSpecificTermDao.getCtcTerm(at.getAgent().getId(), at.getTerm().getId());
        if (l.size() > 0) return true;
        return false;
    }

    /*
    * Persisting ASAE list
    * */
    private void persistASAE(AgentSpecificTerm t) {
        if (!isAgentSpecificTermPersisted(t)) {
            agentSpecificTermDao.save(t);
        }
    }

    private void bootstrap(File inputFile) throws Exception {
        poifs = new POIFSFileSystem(new FileInputStream(inputFile));

        // create a workbook out of the input stream
        wb = new HSSFWorkbook(poifs);
        sh = wb.getSheetAt(0);
        rowCount = sh.getLastRowNum();
        columnsCount = sh.getRow(0).getLastCellNum();

        // get needed headers
        for (byte i = 0; i<columnsCount; i++) {
            HSSFCell cell = sh.getRow(0).getCell(i);
            if (headers.containsKey(cell.getStringCellValue())) {
                headers.remove(cell.getStringCellValue());
                headers.put(cell.getStringCellValue(), Short.valueOf(i));
            }
        }

/*
        for (Map.Entry e : headers.entrySet()) {
            System.out.println(String.format("Header: %s, Position: %d", e.getKey(), Integer.parseInt(e.getValue().toString())));
        }
*/
    }

    // utility method to get contents of cell irrespective of cell type.
    private String getCellData(String sheetname, int rowNum, HSSFCell cell) {
        if (cell == null)
            throw new CaaersSystemException("Invalid data or Blank cell at following location: \n Sheet: " + sheetname + "\n Row: " + (rowNum + 1));

        int cellType = cell.getCellType();
        if (cellType == 0) {
            if (cell.getNumericCellValue() == 0)
                throw new CaaersSystemException("Invalid data or Blank cell at following location: \n Sheet: " + sheetname + "\n Row: " + (rowNum + 1) + "\n Cell: " + ((cell.getCellNum()) + 1));
            return (Integer.toString((int) cell.getNumericCellValue())).trim();
        } else if (cellType == 1) {
            if (cell.getStringCellValue().equals(""))
                throw new CaaersSystemException("Invalid data or Blank cell at following location: \n Sheet: " + sheetname + "\n Row: " + (rowNum + 1) + "\n Cell: " + ((cell.getCellNum()) + 1));
            return cell.getStringCellValue().trim();
        } else {
            throw new CaaersSystemException("Invalid data or Blank cell at following location: \n Sheet: " + sheetname + "\n Row: " + (rowNum + 1) + "\n Cell: " + ((cell.getCellNum()) + 1));
        }

    }


    private HSSFSheet getSheet(String sheetName) {
        HSSFSheet sheet = wb.getSheet(sheetName);
        if (sheet == null)
            throw new CaaersSystemException("\n Unable to find sheet named: " + sheetName + "\n Please fix the error and try again.");
        else
            return sheet;
    }

    public void setAgentDao(AgentDao agentDao) {
        this.agentDao = agentDao;
    }

    public void setTerminologyRepository(TerminologyRepository tRepository) {
        this.terminologyRepository = tRepository;
    }

    public AgentSpecificTermDao getAgentSpecificTermDao() {
        return agentSpecificTermDao;
    }

    public void setAgentSpecificTermDao(AgentSpecificTermDao agentSpecificTermDao) {
        this.agentSpecificTermDao = agentSpecificTermDao;
    }

    public AgentSpecificAdverseEventListService getAsaelService() {
        return asaelService;
    }

    public void setAsaelService(AgentSpecificAdverseEventListService asaelService) {
        this.asaelService = asaelService;
    }
}