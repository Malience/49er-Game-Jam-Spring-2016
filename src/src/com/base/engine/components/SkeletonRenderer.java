package com.base.engine.components;

import com.base.engine.animation.SkeletonLoading.Skeleton;
import com.base.engine.components.attachments.Renderable;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.SkeletonMaterial;
import com.base.engine.rendering.SkinnedMesh;

public class SkeletonRenderer extends GameComponent implements Renderable
{
	private SkinnedMesh mesh;
	private SkeletonMaterial material;
	private Skeleton skeleton;
	
	/**
	 * Creates a component that will be rendered. Must be attached to a GameObject using (GameObject).addComponent((MeshRenderer));
	 * @param mesh The mesh to be rendered upon
	 * @param material The material that will be rendered onto the mesh
	 */
	public SkeletonRenderer(SkinnedMesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = new SkeletonMaterial(material);
		this.material.getBoneValues(mesh);
		this.skeleton = mesh.createSkeleton();
		this.material.setSkeleton(skeleton);
	}

	@Override
	public int render(Shader shader, RenderingEngine renderingEngine)
	{
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
		return 1;
	}

	public void set(SkinnedMesh mesh, Material material) 
	{
		this.mesh = mesh;
		this.material = new SkeletonMaterial(material);
		this.material.getBoneValues(mesh);
		this.skeleton = mesh.createSkeleton();
		this.material.setSkeleton(skeleton);
	}
}
