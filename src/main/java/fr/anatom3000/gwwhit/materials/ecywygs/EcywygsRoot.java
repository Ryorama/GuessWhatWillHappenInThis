package fr.anatom3000.gwwhit.materials.ecywygs;
import net.minecraft.util.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.item.BlockItem;
import fr.anatom3000.gwwhit.CustomItemGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
public class EcywygsRoot  {
public static final Ecywygs ECYWYGS = new Ecywygs();
public static final EcywygsBlock ECYWYGS_BLOCK = new EcywygsBlock();
public static final EcywygsOre ECYWYGS_ORE = new EcywygsOre();
public static final EcywygsShovel ECYWYGS_SHOVEL = new EcywygsShovel(EcywygsMaterial.INSTANCE);
public static final EcywygsPickaxe ECYWYGS_PICKAXE = new EcywygsPickaxe(EcywygsMaterial.INSTANCE);
public static final EcywygsAxe ECYWYGS_AXE = new EcywygsAxe(EcywygsMaterial.INSTANCE);
public static final EcywygsHoe ECYWYGS_HOE = new EcywygsHoe(EcywygsMaterial.INSTANCE);
public static final EcywygsSword ECYWYGS_SWORD = new EcywygsSword(EcywygsMaterial.INSTANCE);

public void onInitialize() {
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs"),ECYWYGS);
Registry.register(Registry.BLOCK, new Identifier("gwwhit", "ecywygs_block"), ECYWYGS_BLOCK);
Registry.register(Registry.ITEM, new Identifier("gwwhit", "ecywygs_block"), new BlockItem(ECYWYGS_BLOCK, new FabricItemSettings().group(CustomItemGroups.GWWHITGroup)));
Registry.register(Registry.BLOCK, new Identifier("gwwhit", "ecywygs_ore"), ECYWYGS_ORE);
Registry.register(Registry.ITEM, new Identifier("gwwhit", "ecywygs_ore"), new BlockItem(ECYWYGS_ORE, new FabricItemSettings().group(CustomItemGroups.GWWHITGroup)));
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs_shovel"),ECYWYGS_SHOVEL);
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs_pickaxe"),ECYWYGS_PICKAXE);
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs_axe"),ECYWYGS_AXE);
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs_hoe"),ECYWYGS_HOE);
Registry.register(Registry.ITEM, new Identifier("gwwhit","ecywygs_sword"),ECYWYGS_SWORD);
RegistryKey<ConfiguredFeature<?,?>> ore = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("gwwhit","ore_ecywygs"));
BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.UNDERGROUND_ORES, ore);
}
public void onInitializeClient() {

}
}