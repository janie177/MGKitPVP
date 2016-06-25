package net.minegusta.heropvp.spells;

import com.google.common.collect.Lists;
import net.minegusta.mglib.utils.EffectUtil;
import org.bukkit.Effect;
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
		new BloodSpell(140, Effect.WITCH_MAGIC, caster.getLocation(), ultimate ? 12 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.DRAGON_BREATH, caster.getUniqueId().toString());
	}

	public static void castFrostSpell(Player caster, Entity targetedEntity, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new FrostSpell(140, Effect.SNOWBALL_BREAK, caster.getLocation(), ultimate ? 10 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.SNOWBALL_BREAK, caster.getUniqueId().toString());
	}

	public static void castFireSpell(Player caster, Entity targetedEntity, boolean ultimate)
	{
		EffectUtil.playSound(caster.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT);
		new FireSpell(140, Effect.MOBSPAWNER_FLAMES, caster.getLocation(), ultimate ? 12 : 7, targetedEntity.getLocation(), targetedEntity, true, true, Effect.MOBSPAWNER_FLAMES, caster.getUniqueId().toString());
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
