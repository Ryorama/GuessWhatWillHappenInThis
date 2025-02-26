package fr.anatom3000.gwwhit.block.entity;

import fr.anatom3000.gwwhit.registry.BlockEntityRegistry;
import fr.anatom3000.gwwhit.registry.BlockRegistry;
import fr.anatom3000.gwwhit.util.MathUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfectedMassBlockEntity extends BlockEntity {
    public static int removeTick = -1;

    private int timer = -1;

    public InfectedMassBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.INFECTED_MASS_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, InfectedMassBlockEntity be) {
        assert world != null;
        if (!world.isClient) {
            if (((ServerWorld) world).getServer().getTicks() == removeTick + 1)
                world.breakBlock(pos, false);

            be.timer = (be.timer + 1) % 20;
            if (be.timer != 0) {
                return;
            }
            if (MathUtil.getChance(75F)) {
                return;
            }
            int completed = 0;

            completed = be.getCompleted(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), completed);
            completed = be.getCompleted(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), completed);
            completed = be.getCompleted(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), completed);
            completed = be.getCompleted(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ()), completed);
            completed = be.getCompleted(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), completed);
            completed = be.getCompleted(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), completed);

            if (completed == 6) {
                world.setBlockState(pos, BlockRegistry.get("inert_infected_mass").getDefaultState());
            }
        }
    }

    private int getCompleted(BlockPos newPos, int completed) {
        BlockState state;
        state = world.getBlockState(newPos);
        if (state.isAir() || state.getBlock() == BlockRegistry.get("infected_mass") || state.getBlock() == BlockRegistry.get("inert_infected_mass")) {
            return completed + 1;
        } else if (MathUtil.getChance(20F)) {
            world.setBlockState(newPos, this.getCachedState());
        }
        return completed;
    }

}
