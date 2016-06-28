package net.minegusta.heropvp.spells;

import com.google.common.collect.Lists;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

public class SpellUtil {

	public static void castBloodSpell(Player caster, Entity targetedEntity, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new HomingBloodSpell(140, Effect.WITCH_MAGIC, caster.getLocation().add(0, 1.3F, 0), ultimate ? 10 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.DRAGON_BREATH, caster.getUniqueId().toString());
	}

	public static void castNormalBloodSpell(Player caster, Location target, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new NormalBloodSpell(140, Effect.SNOWBALL_BREAK, caster.getLocation().add(0, 1.3F, 0).add(caster.getLocation().getDirection().normalize()), ultimate ? 12 : 8, target, true, caster);
	}

	public static void castNormalIceSpell(Player caster, Location target, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new NormalIceSpell(140, Effect.PORTAL, caster.getLocation().add(0, 1.3F, 0).add(caster.getLocation().getDirection().normalize()), ultimate ? 12 : 8, target, true, caster);
	}

	public static void castNormalFireSpell(Player caster, Location target, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new NormalFireSpell(140, Effect.EXTINGUISH, caster.getLocation().add(0, 1.3F, 0).add(caster.getLocation().getDirection().normalize()), ultimate ? 12 : 8, target, true, caster);
	}

	public static void castFrostSpell(Player caster, Entity targetedEntity, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new HomingFrostSpell(140, Effect.SNOWBALL_BREAK, caster.getLocation().add(0, 1.3F, 0), ultimate ? 10 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.SNOWBALL_BREAK, caster.getUniqueId().toString());
	}

	public static void castFireSpell(Player caster, Entity targetedEntity, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new HomingFireSpell(140, Effect.MOBSPAWNER_FLAMES, caster.getLocation().add(0, 1.3F, 0), ultimate ? 10 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.MOBSPAWNER_FLAMES, caster.getUniqueId().toString());
	}

	public static Optional<Player> getTarget(Player player)
	{
		List<Player> players = Lists.newArrayList();
		player.getWorld().getPlayers().stream().filter(pl -> pl.getLocation().distance(player.getLocation()) < 50).forEach(players::add);

		int bestIndex = 0;
		double best = 0;
		int index = 0;

		for(Player p : players)
		{
			Vector toEntity = p.getLocation().toVector().subtract(player.getLocation().toVector());
			Vector direction = player.getLocation().getDirection();
			double dot = toEntity.normalize().dot(direction);

			if(dot > best)
			{
				best = dot;
				bestIndex = index;
			}
			index++;
		}

		if(best > 0.85)
		{
			return Optional.of(players.get(bestIndex));
		}
		return Optional.empty();

	}

}
