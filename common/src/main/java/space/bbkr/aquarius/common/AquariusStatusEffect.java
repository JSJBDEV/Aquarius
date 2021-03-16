package space.bbkr.aquarius.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class AquariusStatusEffect extends StatusEffect {
    public AquariusStatusEffect(StatusEffectType type, int color) {
        super(type, color);
    }

}