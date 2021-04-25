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
	name = "End Match", 
	author = PLUGIN_AUTHOR, 
	description = "Testing CsGO plugins", 
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
	
	HookEvent("cs_win_panel_match", Event_End_Match);
}

public void Event_End_Match(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("Partita finita");
}
