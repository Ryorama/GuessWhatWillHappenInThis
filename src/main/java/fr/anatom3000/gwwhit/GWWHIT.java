package fr.anatom3000.gwwhit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.anatom3000.gwwhit.config.AnnotationExclusionStrategy;
import fr.anatom3000.gwwhit.config.ModConfig;
import fr.anatom3000.gwwhit.registry.BlockEntityRegistry;
import fr.anatom3000.gwwhit.registry.BlockRegistry;
import fr.anatom3000.gwwhit.registry.ItemRegistry;
import fr.anatom3000.gwwhit.registry.NewMaterials;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Random;
import java.util.stream.Collectors;

/*  IMPORTANT NOTICE:
	When adding to this mod make sure you follow proper naming standards:
		Classes 								ThisIsAClass
		Static final fields and enum constants 	THIS_IS_STATIC_FINAL
		Everything else  						thisIsEverythingElse
*/


public class GWWHIT implements ModInitializer {
	//We use a custom ExclusionStrategy to make sure we don't serialize things that break
	public static final Gson GSON = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();

	public static final String MOD_ID = "gwwhit";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static final Random RANDOM = new Random();
	@SuppressWarnings("OptionalGetWithoutIsPresent") //It has to exist exists
	public static final Path ASSETS_ROOT = FabricLoader.getInstance().getModContainer(MOD_ID).get().getPath("assets/gwwhit");

    public static Identifier getId(String path) {
		return new Identifier(MOD_ID, path);
	}

	private static final Identifier LE_BLAZE_LOOT = new Identifier("minecraft", "entities/blaze");
	private static final Identifier LE_BARTER_LOOT = new Identifier("minecraft", "gameplay/piglin_bartering");
	private static final Identifier LE_NEW_BARTER_LOOT = getId("gameplay/new_piglin_barter");

	public static final Identifier CONFIG_SYNC_ID = getId("config_sync");

	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create(MOD_ID + ":data");
	
	public static final Map<String, Map<String, String>> TRANSLATIONS = new HashMap<>();

	private static final FabricLootPoolBuilder POOL_BUILDER = FabricLootPoolBuilder.builder()
			.rolls(UniformLootNumberProvider.create(0, 1))
			.with(ItemEntry.builder(Items.BLAZE_ROD))
			.withCondition(RandomChanceLootCondition.builder(0.38f).build());


	private static final DimensionType MODIFIED_OVERWORLD = DimensionType.create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true,
			-256,
			256,
			256,
			HorizontalVoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getId(),
			DimensionType.OVERWORLD_ID, 0.0F);

	@Override
	public void onInitialize() {
		AutoConfig.register(ModConfig.class, (definition, configClass) -> new GsonConfigSerializer<>(definition, configClass, GSON));

		try {
			for (Path path : Files.list(ASSETS_ROOT.resolve("lang")).collect(Collectors.toList())) {
				String name = path.getFileName().toString();
				name = name.substring(0, name.lastIndexOf('.'));
				try (InputStream is = Files.newInputStream(path); InputStreamReader ir = new InputStreamReader(is)) {
					TRANSLATIONS.put(name, deserialize(ir, new HashMap<>()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ItemRegistry.register();
		BlockRegistry.register();
		BlockEntityRegistry.register();
		Commands.register();
		NewMaterials.INSTANCE.onInitialize();
		registerLootTables();
		registerEvents();
		ModifyWorldHeight();
		LOGGER.info("[GWWHIT] You shouldn't have done this.");
	}
	
	@SuppressWarnings("unchecked") //Stupid IntelliJ
	private <T> T deserialize(Reader r, T current) {
		return GSON.fromJson(r, (Class<T>)current.getClass());
	}

	private void registerLootTables() {
		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {
			if (ModConfig.getLoadedConfig().drops.dreamLuck) {
				if (LE_BLAZE_LOOT.equals(id)) {
					supplier.withPool(POOL_BUILDER.build());
				} else if (LE_BARTER_LOOT.equals(id)) {
					setter.set(manager.getTable(LE_NEW_BARTER_LOOT));
				}
			}
		});
	}

	private void registerEvents() {
		PlayerBlockBreakEvents.AFTER.register(
				(world, player, pos, state, blockEntity) -> {
					if ( ModConfig.getLoadedConfig().blocks.stoneBlocksAreInfected) {
						// AUTHOR: ENDERZOMBI102
						if ( state.getMaterial() == Material.STONE ) {
							SilverfishEntity silverfishEntity = EntityType.SILVERFISH.create(world);
							//noinspection ConstantConditions
							silverfishEntity.refreshPositionAndAngles(
									(double)pos.getX() + 0.5D,
									pos.getY(),
									(double)pos.getZ() + 0.5D,
									0.0F,
									0.0F
							);
							world.spawnEntity(silverfishEntity);
							silverfishEntity.playSpawnEffects();
						}
					}
				}
		);
		RRPCallback.AFTER_VANILLA.register(a -> a.add(RESOURCE_PACK));
	}
  
	private static void ModifyWorldHeight() {
		//OVERWORLD: 14
		Field[] dimension_fields = DimensionType.class.getDeclaredFields();
		for (int i = 0; i < dimension_fields.length; i++) {
			try {
				Resources.makeFieldAccessible(dimension_fields[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(dimension_fields[i].getName() + ", " + i);
		}
		int overworld_num = 14;
		Field overworld_field = dimension_fields[overworld_num];

		try {
			Resources.makeFieldAccessible(overworld_field);
			overworld_field.set(null, MODIFIED_OVERWORLD);
		}catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * OVERWORLD = create(OptionalLong.empty(), true, false, false, true, 1.0D, false, false, true, false, true, -64,
				384, 384, HorizontalVoronoiBiomeAccessType.INSTANCE, BlockTags.INFINIBURN_OVERWORLD.getId(),
				OVERWORLD_ID, 0.0F);
		 */
	}
}





