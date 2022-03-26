package at.petrak.hexcasting.common.casting.mishaps

import at.petrak.hexcasting.api.spell.SpellDatum
import at.petrak.hexcasting.common.casting.CastingContext
import at.petrak.hexcasting.common.casting.colors.FrozenColorizer
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack

class MishapBadOffhandItem(val item: ItemStack, val wanted: Component) : Mishap() {
    override fun accentColor(ctx: CastingContext, errorCtx: Context): FrozenColorizer =
        dyeColor(DyeColor.BROWN)

    override fun execute(ctx: CastingContext, errorCtx: Context, stack: MutableList<SpellDatum<*>>) {
        val item = ctx.caster.getItemInHand(ctx.otherHand).copy()
        ctx.caster.setItemInHand(ctx.otherHand, ItemStack.EMPTY.copy())

        val delta = ctx.caster.lookAngle
        val entity = ItemEntity(
            ctx.world, ctx.caster.x, ctx.caster.eyeY, ctx.caster.z, item,
            delta.x, delta.y, delta.z
        )
        ctx.world.addWithUUID(entity)
    }

    override fun errorMessage(ctx: CastingContext, errorCtx: Context): Component =
        error("bad_item.offhand", actionName(errorCtx.action!!), wanted, item)

    companion object {
        @JvmStatic
        fun of(item: ItemStack, stub: String): MishapBadOffhandItem {
            return MishapBadOffhandItem(item, TranslatableComponent("hexcasting.mishap.bad_item.$stub"))
        }
    }
}