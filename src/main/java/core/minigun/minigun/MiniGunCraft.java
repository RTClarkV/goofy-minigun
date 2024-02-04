package core.minigun.minigun;

import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

public class MiniGunCraft implements Listener {

    private ItemStack miniGun = new ItemStack(Material.CROSSBOW);
    private NamespacedKey gunKey;
    private MiniGun plugin;
    private NamespacedKey bullet;

    BukkitScheduler scheduler;

    public MiniGunCraft(MiniGun plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        bullet = new NamespacedKey(plugin, "Bullet");

        this.scheduler = plugin.getServer().getScheduler();
        gunKey = new NamespacedKey(plugin, "MiniGun");
        ItemMeta meta = miniGun.getItemMeta();
        meta.getPersistentDataContainer().set(gunKey, PersistentDataType.STRING, "GUN");
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD+ "Mini Gun");
        miniGun.setItemMeta(meta);
        

    }
    @EventHandler
    public void onCraft(PrepareItemCraftEvent e){
        if(e.getInventory().contains(new ItemStack(Material.DIAMOND), 8) && e.getInventory().contains(new ItemStack(Material.CROSSBOW))){
            e.getInventory().setResult(miniGun);
        }
    }


    @EventHandler
    public void onReload(EntityLoadCrossbowEvent e){
        if(!(e.getEntity() instanceof Player))return;
        Player p = (Player) e.getEntity();
        if(!p.getInventory().getItemInMainHand().hasItemMeta())return;
        if(!p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(gunKey))return;
        //e.setCancelled(true);

    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(!e.getAction().isRightClick())return;
        e.getPlayer().getInventory().getItemInMainHand();
        if(!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())return;
        if(!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(gunKey))return;
        normalShoot(e.getPlayer());
        e.setCancelled(true);
    }

    public void normalShoot(Player p){
        scheduler.runTaskLater(plugin, ()->{
            p.launchProjectile(Arrow.class).getPersistentDataContainer().set(bullet, PersistentDataType.STRING, "bulletSpeed");
            playSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST, p.getLocation(), 15, 1);
            playParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0, 1.5, 0), 2);
        }, 7L);
        scheduler.runTaskLater(plugin, ()->{
            p.launchProjectile(Arrow.class).getPersistentDataContainer().set(bullet, PersistentDataType.STRING, "bulletSpeed");
            playSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST, p.getLocation(), 15, 1);
            playParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0, 1.5, 0), 2);
        }, 3L);
        //if(!p.getInventory().contains(new ItemStack(Material.ARROW)))return;
        p.launchProjectile(Arrow.class).getPersistentDataContainer().set(bullet, PersistentDataType.STRING, "bulletSpeed");
        playParticle(Particle.CAMPFIRE_COSY_SMOKE, p.getLocation().add(0, 1.5, 0), 2);
        playSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST, p.getLocation(), 15, 1);

    }
    public void playSound(Sound sound, Location location, int volume, int pitch){
        for(Player p2 : location.getNearbyPlayers(50)){
            p2.playSound(location, sound, volume, pitch);
        }
    }
    public void playParticle(Particle particle, Location location, int amount){
        for(Player p2 : location.getNearbyPlayers(50)){
            p2.spawnParticle(particle, location, amount);
        }
    }


    @EventHandler
    public void onHit(ProjectileHitEvent e){
        if(e.getEntity().getType() != EntityType.ARROW)return;
        if(!e.getEntity().getPersistentDataContainer().has(bullet))return;
        e.getEntity().remove();
    }




}
