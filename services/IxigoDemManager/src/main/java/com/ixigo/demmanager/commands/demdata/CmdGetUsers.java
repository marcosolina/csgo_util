package com.ixigo.demmanager.commands.demdata;

import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersResp;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

/**
 * Command dispatched to retrieve a list of users seen play on our CSGO server
 * 
 * @author marco
 *
 */
public class CmdGetUsers implements WebCommandRequest<RestUsersResp> {

}
