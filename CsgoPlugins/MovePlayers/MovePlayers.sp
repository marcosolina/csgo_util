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
	name = "Move Players", 
	author = PLUGIN_AUTHOR, 
	description = "Il will move the players to the specified team", 
	version = PLUGIN_VERSION, 
	url = "https://github.com/marcosolina/csgo_util/blob/main/CsgoPlugins/MovePlayers/MovePlayers.sp"
};

public void OnPluginStart()
{
	g_Game = GetEngineVersion();
	if (g_Game != Engine_CSGO && g_Game != Engine_CSS)
	{
		SetFailState("This plugin is for CSGO/CSS only.");
	}
	
	RegConsoleCmd("sm_move_players", movePlayer, "It will move the players across the teams");
}

public Action movePlayer(int client, int args)
{
	int maxNameLength = 50;
	
	// Loop all the clients
	for (int i = 1; i <= MaxClients; i++)
	{
		// If it is a Human
		if (IsClientConnected(i) && IsClientInGame(i) && !IsFakeClient(i))
		{
			bool terrorist = false;
			char[] playerSteamID = new char[maxNameLength];
			char[] playerName = new char[maxNameLength];
			
			if (GetClientAuthId(i, AuthId_SteamID64, playerSteamID, maxNameLength))
			{
				GetClientName(i, playerName, maxNameLength);
				
				// Search through the input steam IDs
				for (int iArg = 0; iArg < args; iArg++)
				{
					char[] inputSteamID = new char[maxNameLength];
					if (GetCmdArg(iArg, inputSteamID, maxNameLength) > 0)
					{
						if (StrEqual(inputSteamID, playerSteamID))
						{
							terrorist = true;
							break;
						}
					}
				}
				
				if (terrorist)
				{
					CS_SwitchTeam(i, CS_TEAM_T);
					ReplyToCommand(client, "Moved %s to Terrorist Team", playerName);
				}
				else
				{
					CS_SwitchTeam(i, CS_TEAM_CT);
					ReplyToCommand(client, "Moved %s to CT", playerName);
				}
			}
		}
	}
	
	return Plugin_Handled;
}
