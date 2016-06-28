package net.minegusta.heropvp.spells;

import net.minegusta.mglib.particles.MovingParticleWithAction;
import net.minegusta.mglib.utils.EffectUtil;
import net.minegusta.mglib.utils.PotionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class NormalBloodSpell extends MovingParticleWithAction {

	private String casterUUID;
	public NormalBloodSpell(int duration, Effect effect, Location location, double blocksPerSecond, Location target, boolean blocks, Player caster) {
		super(duration, effect, location, blocksPerSecond, target, blocks);
		this.casterUUID = caster.getUniqueId().toString();
	}

	@Override
	public void onHit() {
		location.getWorld().spigot().playEffect(location, Effect.MAGIC_CRIT, 0, 0, 1, 1, 1, 1, 30, 50);
		location.getWorld().spigot().playEffect(location, Effect.PORTAL, 0, 0, 1, 1, 1, 1, 30, 50);
		EffectUtil.playSound(location, Sound.ENTITY_PLAYER_SPLASH);

		try {
			Player caster = Bukkit.getPlayer(UUID.fromString(casterUUID));
			if(caster.isOnline()) {
				location.getWorld().getLivingEntities().stream().filter(le -> le.getLocation().distance(location) < 1.5).forEach(targetedEntity -> {

					EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(caster, targetedEntity, EntityDamageEvent.DamageCause.CUSTOM, 5);
					Bukkit.getPluginManager().callEvent(e);
					if (!e.isCancelled()) {
						PotionUtil.updatePotion(targetedEntity, PotionEffectType.SLOW, 1, 2);
						targetedEntity.damage(e.getFinalDamage());
					}

				});
			}
		} catch (Exception ignored){}
	}
}
