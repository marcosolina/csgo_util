package com.marco.ixigo.demmanager.services.implementations.demprocessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.marco.ixigo.demmanager.model.dto.UserMapStats;
import com.marco.ixigo.demmanager.services.interfaces.DemProcessor;
import com.marco.utils.MarcoException;

public class DemProcessorRasp implements DemProcessor {
    private static final Logger _LOGGER = LoggerFactory.getLogger(DemProcessorRasp.class);

    @Value("${com.marco.ixigo.demmanager.demparser.dotnetexecutable}")
    private String executable;

    @Override
    public List<UserMapStats> processDemFile(File demFile) throws MarcoException {
        _LOGGER.trace("Inside DemProcessorRasp.DemProcessorWindows");

        List<String> cmd = new ArrayList<>();
        cmd.add(executable);
        cmd.add(demFile.getAbsolutePath());

        CmdExecuter ce = new CmdExecuter();
        return ce.extractPlayersScore(cmd);
    }


}
