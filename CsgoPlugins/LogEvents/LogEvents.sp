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
	url = "https://github.com/marcosolina/csgo_util/blob/main/CsgoPlugins/LogEvents/LogEvents.sp"
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
	//HookEvent("start_vote", Event_Start_Vote);
	HookEvent("round_end", Event_Round_End);
	HookEvent("round_announce_warmup", round_announce_warmup);
	//HookEvent("round_announce_match_start", round_announce_match_start);
	//HookEvent("round_officially_ended", round_officially_ended);
	//HookEvent("game_newmap", game_newmap);
	//HookEvent("game_end", game_end);
	//HookEvent("game_start", game_start);
	
}

public void round_announce_warmup(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("round_announce_warmup");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("round_announce_warmup");
   file.Close();
}

public void round_announce_match_start(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("round_announce_match_start");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("round_announce_match_start");
   file.Close();
}

public void round_officially_ended(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("round_officially_ended");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("round_officially_ended");
   file.Close();
}

public void game_newmap(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("game_newmap");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("game_newmap");
   file.Close();
}

public void game_end(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("game_end");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("game_end");
   file.Close();
}

public void game_start(Event event, const char[] name, bool dontBroadcast)
{
   PrintToServer("game_start");
   char[] path = new char[PLATFORM_MAX_PATH];
   BuildPath(Path_SM, path, PLATFORM_MAX_PATH, "event.txt");
   
   File file = OpenFile(path, "w");
   file.WriteLine("game_start");
   file.Close();
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
