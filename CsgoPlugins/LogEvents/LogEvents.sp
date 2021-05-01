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
	name = "Log Events", 
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
	HookEvent("round_start", Event_Round_Start);
	HookEvent("start_vote", Event_Start_Vote);
	HookEvent("round_end", Event_Round_End);
}

public void Event_Round_End(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("Round End");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("Round End");
   file.Close();
}

public void Event_Start_Vote(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("Start Vote");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("Start Vote");
   file.Close();
}

public void Event_End_Match(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("End Map");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("End Map");
   file.Close();
}

public void Event_Round_Start(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("Round Start");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("Round Started");
   file.Close();
   
}
