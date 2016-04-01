package com.base.engine.rendering.MeshLoading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.base.engine.animation.SkeletonLoading.Animation;
import com.base.engine.animation.SkeletonLoading.Bone;
import com.base.engine.animation.SkeletonLoading.KeyFrame;
import com.base.engine.animation.SkeletonLoading.Pose;
import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Transform;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;

public class IQEModel
{
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<OBJIndex> indices;
	private ArrayList<Vector2f> weights;
	private ArrayList<Bone> bones;
	private ArrayList<Animation> animations;
	
	private boolean hasTexCoords;
	private boolean hasNormals;
	
	public IQEModel(String fileName)
	{
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<OBJIndex>();
		weights = new ArrayList<Vector2f>();
		bones = new ArrayList<Bone>();
		animations = new ArrayList<Animation>();
		
		hasTexCoords = false;
		hasNormals = false;
		
		BufferedReader meshReader = null;
		
		try
		{
			meshReader = new BufferedReader(new FileReader(fileName));
			String line;
			String mode = "";
			
			while((line = meshReader.readLine()) != null)
			{
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#"))
					continue;
				else if(tokens[0].equals("vp"))
				{
					positions.add(new Vector3f(Float.valueOf(tokens[1]),
												Float.valueOf(tokens[2]),
												Float.valueOf(tokens[3])));
				}
				else if(tokens[0].equals("vt"))
				{
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]),
							Float.valueOf(tokens[2])));
				}
				else if(tokens[0].equals("vn"))
				{
					normals.add(new Vector3f(Float.valueOf(tokens[1]),
												Float.valueOf(tokens[2]),
												Float.valueOf(tokens[3])));
				}
				else if(tokens[0].equals("vb"))
				{
					for(int i = 1; i < tokens.length; i+=2)
					{
						weights.add(new Vector2f(Float.valueOf(tokens[i]), Float.valueOf(tokens[i+1])));
					}
				}
				else if(tokens[0].equals("fm"))
				{
					for(int i = 0; i < tokens.length - 3; i++)
					{
					indices.add(parseOBJIndex(tokens[1]));
					indices.add(parseOBJIndex(tokens[2 + i]));
					indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
				else if(tokens[0].equals("joint"))
				{
					bones.add(new Bone(
							tokens[1].substring(1, tokens[1].length() - 2),
							Integer.parseInt(tokens[2])
							));
					mode = "joint";
				}
				else if(tokens[0].equals("animation"))
				{
					animations.add(new Animation(tokens[1].substring(1, tokens[1].length() - 2)));
					mode = "animation";
				}
				else if(tokens[0].equals("framerate"))
				{
					animations.get(animations.size()-1).setFramerate(Float.valueOf(tokens[1]));
				}
				else if(tokens[0].equals("frame"))
				{
					animations.get(animations.size()-1).addFrame(new KeyFrame());
				}
				else if(tokens[0].equals("pq") || tokens[0].equals("pm") || tokens[0].equals("pa"))
				{
					Pose pose = new Pose(parseTransform(tokens));
					if(mode.equals("animation"))
					{
						animations.get(animations.size()-1).addPose(pose);
					}
					else if(mode.equals("joint"));
					{
						bones.get(bones.size()-1).setPose(pose);
					}
				}
			}
			
			meshReader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public ArrayList<Animation> toAnimations()
	{
		return animations;
	}
	
	public ArrayList<Bone> toBones()
	{
		return bones;
	}
	
	public ArrayList<Vector2f> toWeights()
	{
		return weights;
	}
	
	public IndexedModel toIndexedModel()
	{
		IndexedModel result = new IndexedModel();
		IndexedModel normalModel = new IndexedModel();
		HashMap<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < indices.size(); i++)
		{
			OBJIndex currentIndex = indices.get(i);
			
			Vector3f currentPosition = positions.get(currentIndex.vertexIndex);
			Vector2f currentTexCoord;
			Vector3f currentNormal;
			
			if(hasTexCoords)
				currentTexCoord = texCoords.get(currentIndex.texCoordIndex);
			else
				currentTexCoord = new Vector2f(0,0);
			
			if(hasNormals)
				currentNormal = normals.get(currentIndex.normalIndex);
			else
				currentNormal = new Vector3f(0,0,0);
			
			
			Integer modelVertexIndex = resultIndexMap.get(currentIndex);
			
			if(modelVertexIndex == null)
			{
				resultIndexMap.put(currentIndex, result.getPositions().size());
				modelVertexIndex = result.getPositions().size();
				
				result.getPositions().add(currentPosition);
				result.getTexCoords().add(currentTexCoord);
				if(hasNormals)
					result.getNormals().add(currentNormal);
			}
			
			Integer normalModelIndex = normalIndexMap.get(currentIndex.vertexIndex);
			
			if(normalModelIndex == null)
			{
				normalIndexMap.put(currentIndex.vertexIndex, normalModel.getPositions().size());
				normalModelIndex = normalModel.getPositions().size();
				
				normalModel.getPositions().add(currentPosition);
				normalModel.getTexCoords().add(currentTexCoord);
				normalModel.getNormals().add(currentNormal);
			}
			
			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
			
		}
		
		if(!hasNormals)
		{
			normalModel.calcNormals();
			
			for(int i = 0; i < result.getPositions().size(); i++)
			{
				result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
//				result.getNormals().get(i).set(normalModel.getNormals().get(indexMap.get(i)));
			}
		}
		
		return result;
	}
	
	private Transform parseTransform(String[] tokens)
	{
		Transform out = new Transform();
		int length = tokens.length - 1;
		if(tokens[0].equals("pq"))
		{
			if(length >= 3)out.setPos(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
			if(length == 4)out.setRot(new Quaternion(0,0,0,1));
			if(length >= 7)out.setRot(new Quaternion(Float.valueOf(tokens[4]), Float.valueOf(tokens[5]), Float.valueOf(tokens[6]), Float.valueOf(tokens[7])));
			if(length == 8)out.setScale(1);
			if(length >= 10)out.setScale(new Vector3f(Float.valueOf(tokens[8]), Float.valueOf(tokens[9]), Float.valueOf(tokens[10])));
		}
		else if(tokens[0].equals("pm"))
		{
			if(length >= 3)out.setPos(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
			if(length == 4)out.setRot(new Quaternion(0,0,0,1));
			if(length >= 12)
			{
				Matrix4f matrix = new Matrix4f();
				matrix.m[0][0] = Float.valueOf(tokens[4]);
				matrix.m[0][1] = Float.valueOf(tokens[5]);
				matrix.m[0][2] = Float.valueOf(tokens[6]);
				matrix.m[1][0] = Float.valueOf(tokens[7]);
				matrix.m[1][1] = Float.valueOf(tokens[8]);
				matrix.m[1][2] = Float.valueOf(tokens[9]);
				matrix.m[2][0] = Float.valueOf(tokens[10]);
				matrix.m[2][1] = Float.valueOf(tokens[11]);
				matrix.m[2][2] = Float.valueOf(tokens[12]);
				out.setRot(new Quaternion(matrix));
			}
			if(length == 13)out.setScale(1);
			if(length >= 15)out.setScale(new Vector3f(Float.valueOf(tokens[13]), Float.valueOf(tokens[14]), Float.valueOf(tokens[15])));
		}
		else if(tokens[0].equals("pa"))
		{
			if(length >= 3)out.setPos(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
			if(length == 4)out.setRot(new Quaternion(0,0,0,1));
			if(length >= 6)out.setRot(new Quaternion(0,0,0,1).rotateByVector(new Vector3f(Float.valueOf(tokens[4]), Float.valueOf(tokens[5]), Float.valueOf(tokens[6]))));
			if(length == 7)out.setScale(1);
			if(length >= 9)out.setScale(new Vector3f(Float.valueOf(tokens[7]), Float.valueOf(tokens[8]), Float.valueOf(tokens[9])));
		}
		return out;
	}
	
	private OBJIndex parseOBJIndex(String token)
	{
		String[] values = token.split("/");
		
		OBJIndex result = new OBJIndex();
		result.vertexIndex = Integer.parseInt(values[0]) - 1;
		
		if(values.length > 1)
		{
			hasTexCoords = true;
			result.texCoordIndex = Integer.parseInt(values[1]) - 1;
			
			if(values.length > 2)
			{
				hasNormals = true;
				result.normalIndex = Integer.parseInt(values[2]) - 1;
			}
		}
		
		return result;
	}
//
//	public ArrayList<Vector3f> getPositions() {
//		return positions;
//	}
//
//	public ArrayList<Vector2f> getTexCoords() {
//		return texCoords;
//	}
//
//	public ArrayList<Vector3f> getNormals() {
//		return normals;
//	}
//
//	public ArrayList<OBJIndex> getIndices() {
//		return indices;
//	}
}
