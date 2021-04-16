#pragma semicolon 1

#define DEBUG

#define PLUGIN_AUTHOR "Marco Solina"
#define PLUGIN_VERSION "0.0.1"

#include <sourcemod>
#include <sdktools>
#include <cstrike>
//#include <sdkhooks>

#pragma newdecls required

EngineVersion g_Game;

public Plugin myinfo = 
{
	name = "Move Players",
	author = PLUGIN_AUTHOR,
	description = "Testing CsGO plugins",
	version = PLUGIN_VERSION,
	url = "https://github.com/marcosolina/csgo_util/blob/main/CsgoPlugins/MovePlayers/MovePlayers.sp"
};

public void OnPluginStart()
{
	g_Game = GetEngineVersion();
	if(g_Game != Engine_CSGO && g_Game != Engine_CSS)
	{
		SetFailState("This plugin is for CSGO/CSS only.");	
	}
	
	RegConsoleCmd("sm_move_players", movePlayer, "It will move the players across the teams");
}

public Action movePlayer(int client, int args)
{
	ReplyToCommand(client, "Number of arguments: %d", args);
	int maxNameLength = 50;
	
	for (int iArg = 0; iArg < args; iArg++){
		char[] inputName = new char[maxNameLength];
		int charsWritten = GetCmdArg(iArg, inputName, maxNameLength);
		
		ReplyToCommand(client, "Processing Param: %s", inputName);
		if(charsWritten > 0)
		{
			for (int i = 1; i <= MaxClients; i++)
			{
			    if (IsClientConnected(i) && IsClientInGame(i) && !IsFakeClient(i))
			    {
			    	char[] gamePName = new char[maxNameLength];
			    	GetClientName(i, gamePName, maxNameLength);
			    	
			    	if(StrEqual(inputName, gamePName)){
			    		ReplyToCommand(client, "Moving %s to the Terrorist Team", gamePName);
			    		CS_SwitchTeam(i, CS_TEAM_T);
			    	}else{
			    		ReplyToCommand(client, "Moving %s to the CT Team", gamePName);
			    		CS_SwitchTeam(i, CS_TEAM_CT);
			   		}
			    }
			}
		}else{
			ReplyToCommand(client, "Not Able to read argument");
		}
	}
	ReplyToCommand(client, "Plugin Execution completed");
	
	return Plugin_Handled;
}
