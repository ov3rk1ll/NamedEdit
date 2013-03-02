package com.ov3rk1ll.nse;

import org.bukkit.plugin.java.JavaPlugin;

import com.ov3rk1ll.nse.command.EditSignCommand;



public class NamedSignEditPlugin extends JavaPlugin {
	public static String TAG = "NamedSignEdit";
	public static String CHATTAG = "[" + TAG + "]";
	
	private EditSignCommand cc;

	public void onEnable() {
		saveDefaultConfig();
		this.cc = new EditSignCommand(this);
		getCommand("nse").setExecutor(this.cc);
		//getCommand("namedsignedit").setExecutor(this.cc);
	}

	public void onDisable(){
		saveConfig();
	}
}