package com.dannbrown.friendcompass.init

import com.dannbrown.deltaboxlib.init.DeltaboxRegistrate
import dev.architectury.registry.CreativeTabRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object ModContent {
  const val MOD_ID = "friendcompass"
  var REGISTRATE = DeltaboxRegistrate(MOD_ID)

//  val TAB = REGISTRATE.creativeTab(MOD_ID, "Friend Compass", { ItemStack(ModItems.MOD_ICON.get()) }, { p, o ->
//    CreativeTabsUtil.displayAll(
//      REGISTRATE, p, o
//    )
//  })

  fun init() {
    ModConfig.register()
    ModSounds.register()
    ModTags.register()
    ModBlocks.register()
    ModEntityTypes.register()
    ModItems.register()
    ModParticles.register()
    ModConfiguredFeatures.register()
    ModPlacedFeatures.register()
    ModBlockEntities.register()
    ModModelLayers.register()
    ModPlacerTypes.register()
    ModLang.register()
    ModBiomeModifiers.register()
    REGISTRATE.buildRegistries()

    CreativeTabRegistry.modifyBuiltin(
      BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.TOOLS_AND_UTILITIES),
      { flags, output, canUseGameMasterBlocks ->
        output.acceptAfter(Items.COMPASS, ItemStack(ModItems.FRIEND_COMPASS.get()));
      })
  }
}