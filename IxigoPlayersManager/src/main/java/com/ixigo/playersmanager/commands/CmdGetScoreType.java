package com.ixigo.playersmanager.commands;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.playersmanagercontract.models.rest.RestScoreTypes;

/**
 * Command dispatched to retrieve a list of score types available in the system
 * 
 * @author marco
 *
 */
public class CmdGetScoreType implements WebCommandRequest<RestScoreTypes> {

}