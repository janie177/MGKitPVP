package net.minegusta.heropvp.spells;

import net.minegusta.mglib.particles.AbstractTargetingParticleEffect;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class HomingBloodSpell extends AbstractTargetingParticleEffect {

	private String casterUUID;

	public HomingBloodSpell(int duration, Effect effect, Location location, double blocksPerSecond, Location target, Entity targetedEntity, boolean removeOnHit, boolean removeOnBlock, Effect particleOnHit, String casterUUID) {
		super(duration, effect, location, blocksPerSecond, target, targetedEntity, removeOnHit, removeOnBlock, particleOnHit);
		this.casterUUID = casterUUID;
	}

	@Override
	public void onHit(Entity targetedEntity) {
		EffectUtil.playSound(targetedEntity.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		try {
			Player caster = Bukkit.getPlayer(UUID.fromString(casterUUID));
			if(caster.isOnline())
			{
				EntityDamageByEntityEvent e = new EntityDamageByEntityEvent(caster, targetedEntity, EntityDamageEvent.DamageCause.CUSTOM, 7);
				Bukkit.getPluginManager().callEvent(e);
				if(!e.isCancelled())
				{
					LivingEntity le = (LivingEntity) targetedEntity;
					double maxHealed = caster.getMaxHealth() - caster.getHealth();
					if(maxHealed > 7) maxHealed = 7;
					caster.setHealth(caster.getHealth() + maxHealed);
					le.damage(e.getFinalDamage());
				}
			}

		} catch (Exception ignored){}
	}

	public String getCasterUUID() {
		return casterUUID;
	}
}
