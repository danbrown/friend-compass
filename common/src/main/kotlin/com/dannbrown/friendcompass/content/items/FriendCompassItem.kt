package com.dannbrown.friendcompass.content.items

import com.dannbrown.friendcompass.LoaderCompat
import com.dannbrown.friendcompass.init.ModContent
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class FriendCompassItem(props: Properties) : Item(props.durability(200)) {
  fun getMaxDamage(stack: ItemStack?): Int {// forge soft override
    return 50
  }

  companion object {
    val OWNER_TAG = ModContent.MOD_ID + ":ownerName"
    val OFFLINE_KEY = "item.${ModContent.MOD_ID}.friend_compass.offline"
    val DIMENSION_KEY = "item.${ModContent.MOD_ID}.friend_compass.dimension"
    val SELF_KEY = "item.${ModContent.MOD_ID}.friend_compass.self"
    val DESCRIPTION_KEY = "item.${ModContent.MOD_ID}.friend_compass.description"
    val IS_LINKED_TO_KEY = "item.${ModContent.MOD_ID}.friend_compass.is_linked_to"
  }

  override fun appendHoverText(
    pStack: ItemStack,
    pLevel: Level?,
    pTooltipComponents: MutableList<Component>,
    pIsAdvanced: TooltipFlag
  ) {
    super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    pTooltipComponents.add(Component.translatable(DESCRIPTION_KEY).withStyle(ChatFormatting.GRAY))

    if (pStack.hasTag() && pStack.tag != null && pStack.tag!!
        .contains(OWNER_TAG)
    ) {
      val owner = pStack.tag!!
        .getString(OWNER_TAG)
      pTooltipComponents.add(Component.literal(""))
      pTooltipComponents.add(
        Component.translatable(IS_LINKED_TO_KEY, ChatFormatting.GOLD.toString() + owner).withStyle(ChatFormatting.GRAY)
      )
    }
  }

  override fun use(level: Level, player: Player, handIn: InteractionHand): InteractionResultHolder<ItemStack> {
    val itemStack: ItemStack = player.getItemInHand(handIn)

    if (!level.isClientSide && itemStack.hasTag() && itemStack.tag!!.contains(OWNER_TAG)) {
      val ownerName = itemStack.tag!!.getString(OWNER_TAG)

      // Player is the owner, no need to teleport
      if (ownerName.equals(player.gameProfile.name, ignoreCase = true)) {
        player.sendSystemMessage(Component.translatable(SELF_KEY))
        return InteractionResultHolder(InteractionResult.SUCCESS, itemStack)
      }

      // find the owner player
      var owner: Player? = null
      if (level.server != null) {
        level as ServerLevel
        for (player1 in level.server.playerList.players) {
          if (player1.gameProfile.name.equals(ownerName, ignoreCase = true)) {
            owner = player1
            break
          }
        }
      }

      // Player is offline, can't teleport
      if (owner == null) {
        player.sendSystemMessage(Component.translatable(OFFLINE_KEY, ChatFormatting.RED.toString() + ownerName))
        return InteractionResultHolder(InteractionResult.FAIL, itemStack)
      }

      val isInAnotherDimension = owner.level().dimension().location() !== player.level().dimension().location()

      // Player is in another dimension, teleport only if sneaking
      if (isInAnotherDimension && !player.isShiftKeyDown) {
        player.sendSystemMessage(Component.translatable(DIMENSION_KEY, ChatFormatting.RED.toString() + ownerName))
        return InteractionResultHolder(InteractionResult.FAIL, itemStack)
      }

      if (!player.abilities.instabuild) {
        itemStack.hurtAndBreak(1, player) { player1: LivingEntity -> player1.broadcastBreakEvent(handIn) }
      }
      val fakePlayerClass = LoaderCompat.getFakePlayer()
      if (!fakePlayerClass.isAssignableFrom(player.javaClass)) {
        level.playSound(
          null as Player?,
          player.x,
          player.y,
          player.z,
          SoundEvents.CHORUS_FRUIT_TELEPORT,
          SoundSource.NEUTRAL,
          0.5f,
          0.4f / (level.random.nextFloat() * 0.4f + 0.8f)
        )
        player.cooldowns.addCooldown(this, 20)

        // extra-dimensional teleport
        if (isInAnotherDimension) {
          val target = owner.level() as ServerLevel
          player.teleportTo(target, owner.x, owner.y, owner.z, setOf(), player.yRot, player.xRot)
        }
        // normal teleport
        else {
          player.teleportTo(owner.x, owner.y, owner.z)
        }
      }

      return InteractionResultHolder(InteractionResult.SUCCESS, itemStack)
    } else {
      return InteractionResultHolder(InteractionResult.FAIL, itemStack)
    }
  }

  override fun inventoryTick(stack: ItemStack, level: Level, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
    if (!level.isClientSide) {
      if ((stack.hasTag() && stack.tag != null && !stack.tag!!.contains(OWNER_TAG)) || !stack.hasTag() || (stack.tag == null)) {
        val tag = if (stack.tag == null) CompoundTag() else stack.tag!!
        val fakePlayerClass = LoaderCompat.getFakePlayer()
        if (entityIn is Player && !fakePlayerClass.isAssignableFrom(entityIn.javaClass)) {
          tag.putString(OWNER_TAG, entityIn.gameProfile.name)

          stack.tag = tag
        }
      }
    }
    super.inventoryTick(stack, level, entityIn, itemSlot, isSelected)
  }

  override fun getName(stack: ItemStack): Component {
    if (stack.hasTag() && stack.tag != null && stack.tag!!.contains(OWNER_TAG)) {
      val tag = stack.tag
      val owner = tag!!.getString(OWNER_TAG)
      return Component.literal("$owner's ").append(Component.translatable(this.getDescriptionId(stack)))
    } else {
      return super.getName(stack)
    }
  }
}