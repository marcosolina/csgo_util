#pragma semicolon 1

#define DEBUG

#define PLUGIN_AUTHOR "Marco Solina"
#define PLUGIN_VERSION "1.0.0"

#include <sourcemod>
#include <sdktools>
#include <cstrike>
//#include <sdkhooks>

#pragma newdecls required

EngineVersion g_Game;

public Plugin myinfo = 
{
	name = "List Players", 
	author = PLUGIN_AUTHOR, 
	description = "It will return a list of the active players on the CSGO server", 
	version = PLUGIN_VERSION, 
	url = "https://github.com/marcosolina/csgo_util/blob/main/CsgoPlugins/ListPlayers/ListPlayers.sp"
};

public void OnPluginStart()
{
	g_Game = GetEngineVersion();
	if (g_Game != Engine_CSGO && g_Game != Engine_CSS)
	{
		SetFailState("This plugin is for CSGO/CSS only.");
	}
	
	RegConsoleCmd("sm_list_players", movePlayer, "It will list the steam IDs of the players");
}

public Action movePlayer(int client, int args)
{
	int maxNameLength = 50;
	
	ReplyToCommand(client, "TERRORISTS");
	
	// Loop all the clients
	for (int i = 1; i <= MaxClients; i++)
	{
		// If it is a Human
		if (IsClientConnected(i) && IsClientInGame(i) && !IsFakeClient(i))
		{
			char[] playerSteamID = new char[maxNameLength];
			
			if (GetClientAuthId(i, AuthId_SteamID64, playerSteamID, maxNameLength))
			{
				// Print only the Terrorist players
				if(GetClientTeam(i) == CS_TEAM_T){
					ReplyToCommand(client, playerSteamID);
				}
			}
		}
	}
	
	ReplyToCommand(client, "CT");
	
	// Loop all the clients
	for (int i = 1; i <= MaxClients; i++)
	{
		// If it is a Human
		if (IsClientConnected(i) && IsClientInGame(i) && !IsFakeClient(i))
		{
			char[] playerSteamID = new char[maxNameLength];
			
			if (GetClientAuthId(i, AuthId_SteamID64, playerSteamID, maxNameLength))
			{
				// Print only the CT players
				if(GetClientTeam(i) == CS_TEAM_CT){
					ReplyToCommand(client, playerSteamID);
				}
			}
		}
	}
	
	return Plugin_Handled;
}
