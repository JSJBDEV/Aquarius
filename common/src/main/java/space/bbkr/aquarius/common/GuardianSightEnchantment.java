package space.bbkr.aquarius.common;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;

public class GuardianSightEnchantment extends Enchantment {
	protected GuardianSightEnchantment() {
		super(Rarity.COMMON, EnchantmentTarget.TRIDENT, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public int getMaxPower(int level) {
		return 1 + (level - 1) * 10;
	}

	@Override
	protected boolean canAccept(Enchantment enchantment) {
		return super.canAccept(enchantment) && enchantment != Enchantments.RIPTIDE && enchantment != Enchantments.CHANNELING && enchantment != Enchantments.LOYALTY;
	}
}
