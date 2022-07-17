package cartmod.mixin;

import cartmod.RailHitboxHelper;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.RailShape;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RailBlock.class)
public abstract class RailBlockMixin extends AbstractRailBlock{
    @Shadow @Final public static EnumProperty<RailShape> SHAPE;

    protected RailBlockMixin(boolean forbidCurves, Settings settings) {
        super(forbidCurves, settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape railCollisionShape = super.getOutlineShape(state, world, pos, context);
        return RailHitboxHelper.getOutlineShape(railCollisionShape, state, state.get(SHAPE), world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape railCollisionShape = super.getCollisionShape(state, world, pos, context);
        return RailHitboxHelper.getCollisionShape(railCollisionShape, state.get(SHAPE), state, world, pos, context);
    }
}
