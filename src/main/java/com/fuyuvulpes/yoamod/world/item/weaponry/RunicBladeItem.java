package com.fuyuvulpes.yoamod.world.item.weaponry;

import com.fuyuvulpes.yoamod.world.item.AttackAnim;
import com.fuyuvulpes.yoamod.world.item.AttackAnims;
import com.fuyuvulpes.yoamod.world.item.WeaponItem;
import com.fuyuvulpes.yoamod.world.item.WeaponStats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class RunicBladeItem extends WeaponItem {
    public RunicBladeItem(Tier tier, Properties properties) {
        super(tier, WeaponStats.RUNIC_BLADE, properties);
    }

    @Override
    public AttackAnim getAttackAnimation(ItemStack itemStack) {
        return AttackAnims.SWING;
    }
}
