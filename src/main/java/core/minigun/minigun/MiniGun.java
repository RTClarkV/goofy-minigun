package core.minigun.minigun;

import org.bukkit.plugin.java.JavaPlugin;

public final class MiniGun extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new MiniGunCraft(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
