package net.minegusta.heropvp.spells;

import net.minegusta.mglib.particles.MovingParticleWithAction;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class NormalFireSpell extends MovingParticleWithAction {

	private String casterUUID;
	public NormalFireSpell(int duration, Effect effect, Location location, double blocksPerSecond, Location target, boolean blocks, Player caster) {
		super(duration, effect, location, blocksPerSecond, target, blocks);
		this.casterUUID = caster.getUniqueId().toString();
	}

	@Override
	public void onHit() {
		location.getWorld().spigot().playEffect(location, Effect.FLAME, 0, 0, 1, 1, 1, 1, 30, 50);
		location.getWorld().spigot().playEffect(location, Effect.MOBSPAWNER_FLAMES, 0, 0, 1, 1, 1, 1, 30, 50);
		EffectUtil.playSound(location, Sound.ENTITY_ENDERDRAGON_FIREBALL_EXPLODE);

		try {
			Player caster = Bukkit.getPlayer(UUID.fromString(casterUUID));
			if(caster.isOnline()) {
				location.getWorld().getLivingEntities().stream().filter(le -> le.getLocation().distance(location) < 1.5).forEach(targetedEntity -> {

					EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(caster, targetedEntity, EntityDamageEvent.DamageCause.CUSTOM, 4);
					Bukkit.getPluginManager().callEvent(e);
					if (!e.isCancelled()) {
						targetedEntity.setFireTicks(30);
						targetedEntity.damage(e.getFinalDamage());
					}

				});
			}
		} catch (Exception ignored){}
	}
}
