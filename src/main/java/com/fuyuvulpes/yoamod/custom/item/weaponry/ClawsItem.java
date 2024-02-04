package com.fuyuvulpes.yoamod.custom.item.weaponry;

import com.fuyuvulpes.yoamod.core.items.WeaponItem;
import com.fuyuvulpes.yoamod.core.items.WeaponStats;
import net.minecraft.world.item.Tier;

public class ClawsItem extends WeaponItem {
    public ClawsItem(Tier tier, Properties properties) {
        super(tier, WeaponStats.CLAWS, properties);
    }
}