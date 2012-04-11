package gov.nih.nci.cabig.caaers.service.synchronizer;

import edu.nwu.bioinformatics.commons.CollectionUtils;
import gov.nih.nci.cabig.caaers.domain.Study;
import gov.nih.nci.cabig.caaers.domain.StudyDevice;
import gov.nih.nci.cabig.caaers.service.DomainObjectImportOutcome;
import gov.nih.nci.cabig.caaers.service.migrator.Migrator;

import java.util.HashMap;

/**
 * @author Ion C. Olaru
 *         Date: 4/10/12 -4:37 PM
 */
public class StudyDeviceSynchronizer implements Migrator<Study> {

    public void migrate(Study src, Study dest, DomainObjectImportOutcome<Study> studyDomainObjectImportOutcome) {
        if (CollectionUtils.isEmpty(src.getStudyDevices())) return;

		HashMap<String, StudyDevice> map = new HashMap<String, StudyDevice>();
		for(StudyDevice sd : dest.getActiveStudyDevices()) {
            String key = (sd.getDevice() != null ? sd.getDevice().getCommonName() : sd.getCommonName());
			map.put(key, sd);
		}

        for (StudyDevice sd : src.getStudyDevices()) {
            String key = (sd.getDevice() != null ? sd.getDevice().getCommonName() : sd.getCommonName());
            StudyDevice studyDevice = map.get(key);

            if (studyDevice == null) {
                // ADD NEW
                dest.addStudyDevice(sd);
            } else {
                // UPDATE
                studyDevice.setDevice(sd.getDevice());
                studyDevice.setCommonName(sd.getCommonName());
                studyDevice.setBrandName(sd.getBrandName());
                studyDevice.setDeviceType(sd.getDeviceType());
            }
        }
    }

}