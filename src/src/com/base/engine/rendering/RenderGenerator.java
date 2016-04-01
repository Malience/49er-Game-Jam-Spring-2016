package com.base.engine.rendering;

import com.base.engine.components.MeshRenderer;

public class RenderGenerator 
{
	private Material material;
	private String mesh;
	
	public RenderGenerator(String mesh) {this(mesh, "test.png");}
	public RenderGenerator(String mesh, String texture) {this(mesh, new Texture(texture));}
	public RenderGenerator(String mesh, Texture texture)
	{
		material = new Material();
		material.addTexture("diffuse", texture);
		this.mesh = mesh;
	}
	
	public RenderGenerator(String mesh, String texture, MaterialAttachment attach) {this(mesh, new Texture(texture), attach);}
	public RenderGenerator(String mesh, Texture texture, MaterialAttachment attach)
	{
		material = new Material();
		material.addTexture("diffuse", texture);
		attach.apply(material);
		this.mesh = mesh;
	}
	
	public RenderGenerator(String mesh, String texture, MaterialAttachment[] attach) {this(mesh, new Texture(texture), attach);}
	public RenderGenerator(String mesh, Texture texture, MaterialAttachment[] attach)
	{
		material = new Material();
		material.addTexture("diffuse", texture);
		for(int i = 0; i < attach.length; i++)
		{
			attach[i].apply(material);
		}
		this.mesh = mesh;
	}
	
	public MeshRenderer generate()
	{
		return new MeshRenderer(new Mesh(mesh), material);
	}
	
	public MeshRenderer generate(Mesh mesh)
	{
		return new MeshRenderer(mesh, material);
	}
}
