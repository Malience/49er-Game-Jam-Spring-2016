package com.base.engine.physics.collision;

import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.physics.collision.broadphase.AABB;

public class IntersectionTests 
{
	public static boolean AABBandAABB(AABB one, AABB two)
	{
		float d1x = two.min.x - one.max.x;
		float d1y = two.min.y - one.max.y;
		float d1z = two.min.z - one.max.z;
		float d2x = one.min.x - two.max.x;
		float d2y = one.min.y - two.max.y;
		float d2z = one.min.z - two.max.z;
		
		return
		d1x > 0 && d1y > 0 && d1z > 0 &&
		d2x > 0 && d2y > 0 && d2z > 0;
	}
	
	public static boolean sphereAndHalfSpace(Sphere sphere, Plane plane)
	{
		float ballDistance = plane.getDirection().dot(sphere.getAxis(3)) - sphere.radius;
		return ballDistance <= plane.getOffset();
	}
	
	public static boolean sphereAndHalfSpace(Vector3f pos, float radius, Plane plane)
	{
		float ballDistance = plane.getDirection().dot(pos) - radius;
		return ballDistance <= plane.getOffset();
	}
	
	public static boolean sphereAndSphere(Sphere one, Sphere two)
	{
		Vector3f midline = one.getAxis(3).sub(two.getAxis(3));
		return midline.magnitude()*midline.magnitude() < (one.radius + two.radius)*(one.radius + two.radius);
	}
	
	public static boolean sphereAndSphere(Sphere one, Vector3f pos, float radius)
	{
		Vector3f distance = one.getAxis(3).sub(pos);
		return distance.magnitude()*distance.magnitude() < (one.radius + radius)*(one.radius + radius);
	}
	
	public static boolean sphereAndSphere(Vector3f pos1, float radius1, Vector3f pos2, float radius2)
	{
		Vector3f distance = pos1.sub(pos2);
		return distance.magnitude()*distance.magnitude() < (radius1 + radius2)*(radius1 + radius2);
	}
	
	public static boolean boxAndBox(Box one, Box two)
	{
		Vector3f toCentre = two.getAxis(3).sub(one.getAxis(3));
		return 
				overlapOnAxis(one, two, (one.getAxis(0)) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(1)) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(2)) , toCentre) &&
				
				overlapOnAxis(one, two, (two.getAxis(0)) , toCentre) &&
				overlapOnAxis(one, two, (two.getAxis(1)) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(2)) , toCentre) &&
				
				overlapOnAxis(one, two, (one.getAxis(0).cross(two.getAxis(0))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(0).cross(two.getAxis(1))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(0).cross(two.getAxis(2))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(1).cross(two.getAxis(0))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(1).cross(two.getAxis(1))) , toCentre) && 
				overlapOnAxis(one, two, (one.getAxis(1).cross(two.getAxis(2))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(2).cross(two.getAxis(0))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(2).cross(two.getAxis(1))) , toCentre) &&
				overlapOnAxis(one, two, (one.getAxis(2).cross(two.getAxis(2))) , toCentre)
				;
	}
	
	public static boolean boxAndHalfSpace(Box box, Plane plane)
	{
		float projectedRadius = transformToAxis(box, plane.getDirection());
		
		float boxDistance = plane.getDirection().dot(box.getAxis(3)) - projectedRadius;
		return boxDistance <= plane.getOffset();
	}
	
	/**
	 * 	Simple ray sphere collision detection
	 * @param p The ray's origin - sphere's center
	 * @param d	Direction of the ray, normalize it first
	 * @param r	Radius of the sphere
	 * @param i Bucket vector, x contains the first intersection distance and y contains the second where 1st <= second
	 * @return True if there is an intersection false if there isn't
	 */
	public static boolean rayAndSphere(Vector3f p, Vector3f d, float r, Vector2f i)
	{
		float det, b;
		b = -p.dot(d);
		det = b*b - p.dot(p) + r*r;
		if(det < 0) return false;
		det = (float)Math.sqrt(det);
		i.x = b - det;
		i.y = b + det;
		if(i.y < 0) return false;
		if(i.x < 0) i.x = 0;
		return true;
	}
	
	public static boolean rayAndBox(Vector3f ray, Vector3f direction, Box box, Vector2f output)
	{
		Vector3f max = box.getTransform().getPos().add(box.getHalfSize());
		Vector3f min = box.getTransform().getPos().sub(box.getHalfSize());
		Matrix4f transform = box.getTransform().getTransformation();
//		max = transform.transform(max);
//		min = transform.transform(min);
//		ray = transform.transform(ray);
//		direction = transform.transformDirection(direction);
		float tMin = 0;
		float tMax = 100000;
		
		Vector3f OBBposition_worldspace = new Vector3f(transform.m[3][0], transform.m[3][1], transform.m[3][2]);
		Vector3f delta = OBBposition_worldspace.sub(ray);
		
		Vector3f xaxis = new Vector3f(transform.m[0][0], transform.m[0][1], transform.m[0][2]);
		float e = xaxis.dot(delta);
		float f = direction.dot(xaxis);
		
		float t1 = (e+min.x)/f;
		float t2 = (e+max.x)/f;
		if(t1>t2){float w = t1; t1=t2; t2=w;}
		
		if(t2 < tMax) tMax = t2;
		if(t1 > tMin) tMin = t1;
		
		if(tMax < tMin) return false;
		output.x = tMin;
		output.y = tMax;
		return true;
		
	}
	
	public static float transformToAxis(Box box, Vector3f axis)
	{
		return
				box.getHalfSize().x * Math.abs(axis.dot(box.getAxis(0))) +
				box.getHalfSize().y * Math.abs(axis.dot(box.getAxis(1))) +
				box.getHalfSize().z * Math.abs(axis.dot(box.getAxis(2)));
	}
	
	public static boolean overlapOnAxis(Box one, Box two, Vector3f axis, Vector3f toCentre)
	{
		float oneProject = transformToAxis(one, axis);
		float twoProject = transformToAxis(two, axis);
		
		float distance = Math.abs(toCentre.dot(axis));
		
		return (distance < oneProject + twoProject);
	}
	
	public static float penetrationOnAxis(Box one, Box two, Vector3f axis, Vector3f toCentre)
	{
		float oneProject = transformToAxis(one, axis);
		float twoProject = transformToAxis(two, axis);
		
		float distance = Math.abs(toCentre.dot(axis));
		
		return oneProject + twoProject - distance;
	}
	
	public static boolean tryAxis(Box one, Box two, Vector3f axis, Vector3f toCentre, int index, Vector2f pen)
	{
		if(axis.magnitude()*axis.magnitude() < 0.0001) return true;
		axis.normalize();
		
		float penetration = penetrationOnAxis(one, two, axis, toCentre);
		
		if(penetration < 0) return false;
		if(penetration < pen.x)
		{
			pen.x = penetration;
			pen.y = index;
		}
		return true;
	}
	
	public static Vector3f contactPoint(Vector3f pone, Vector3f done, float oneSize, Vector3f ptwo, Vector3f dtwo, float twoSize, boolean useOne)
	{
		Vector3f tost, cone, ctwo;
		
		float dpStaOne, dpStaTwo, dpOneTwo, smOne, smTwo;
		float denom, mua, mub;
		
		smOne = done.magnitude()*done.magnitude();
		smTwo = dtwo.magnitude()*dtwo.magnitude();
		dpOneTwo = dtwo.dot(done);
		
		tost = pone.sub(ptwo);
		dpStaOne = done.dot(tost);
		dpStaTwo = dtwo.dot(tost);
		
		denom = smOne * smTwo - dpStaOne * dpStaTwo;
		
		if(Math.abs(denom) < 0.0001f)
		{
			return useOne?pone:ptwo;
		}
		
		mua = (dpOneTwo * dpStaTwo - smTwo * dpStaOne) / denom;
		mub = (smOne * dpStaTwo - dpOneTwo * dpStaOne) / denom;
		
		if(mua > oneSize || mua < -oneSize || mub > twoSize || mub < -twoSize)
		{
			return useOne?pone:ptwo;
		}
		else
		{
			cone = pone.add(done.mul(mua));
			ctwo = ptwo.add(dtwo.mul(mub));
			
			return cone.mul(.5f).add(ctwo.mul(.5f));
		}
		
	}
	
	public static boolean separatingAxisTheorem(ConvexHull hull1, ConvexHull hull2)
	{
		//TODO
		return false;
	}
	
	// Returns the squared distance between point c and segment ab
	public static float sqDistPointSegment(Vector3f a, Vector3f b, Vector3f c)
	{
		Vector3f ab = b.sub(a), ac = c.sub(a), bc = c.sub(b);
		float e = ac.dot(ab);
		// Handle cases where c projects outside ab
		if (e <= 0.0f) return ac.dot(ac);
		float f = ab.dot(ab);
		if (e >= f) return bc.dot(bc);
		// Handle cases where c projects onto ab
		return (ac.dot(ac))-((e*e) / f);
	}
	
	static Vector3f closestPointOnSegment(Vector3f a, Vector3f b, Vector3f c)
	{
		Vector3f ab = b.sub(a);
		Vector3f ac = c.sub(a);
		float e = ac.dot(ab);
		// Handle cases where c projects outside ab
		if (e <= 0.0f) return a;
		float f = ab.dot(ab);
		if (e >= f) return b;
		// Handle cases where c projects onto ab
		return c.projection(ab);
	}
	
	
	public static void closestPointLineLine(Vector3f v0, Vector3f v10, Vector3f v2, Vector3f v32, Vector2f out)
	{
		Vector3f v02 = v0.sub(v2);
		float d0232 = v02.dot(v32);
		float d3210 = v32.dot(v10);
		float d3232 = v32.dot(v32);
		float d0210 = v02.dot(v10);
		float d1010 = v10.dot(v10);
		float denom = d1010*d3232 - d3210*d3210;
		if(denom != 0) out.x = (d0232*d3210 - d0210*d3232) / denom;
		else out.x = 0;
		out.y = (d0232 + out.x*d3210) / d3232;
	}
	static float closestPoint(Vector3f a, Vector3f b, Vector3f p, float d, Vector3f out)
	{
		Vector3f dir = b.sub(a);
		d = p.sub(a).dot(dir) / (dir.length() * dir.length());
		out = a.add(dir.mul(d));
		return d;
	}
	
	static Vector3f closestPoint(Vector3f a1, Vector3f b1, Vector3f a2, Vector3f b2, Vector2f out)
 	{
         Vector3f dir = b1.sub(a1);
         closestPointLineLine(a1, b1.sub(a1), a2, b2.sub(a2), out);
         if (out.x >= 0.f && out.x <= 1.f && out.y >= 0.f && out.y <= 1.f)
                 return a1.add(dir.mul(out.x));
         else if (out.x >= 0.f && out.x <= 1.f) // Only d2 is out of bounds.
         {
                 Vector3f p;
                 if (out.y < 0.f)
                 {
                         out.y = 0.f;
                         p = a2;
                 }
                 else
                 {
                         out.y = 1.f;
                         p = b2;
                 }
                 Vector3f o = null;
                 out.x = closestPoint(a1, b1, p, out.x, o);
                 return o;
         }
         else if (out.y >= 0.f && out.y <= 1.f) // Only d is out of bounds.
         {
        	 	Vector3f p;
                 if (out.x < 0.f)
                 {
                         out.x = 0.f;
                         p = a1;
                 }
                 else
                 {
                         out.x = 1.f;
                         p = b1;
                 }
 
                 Vector3f o = null;
                 out.x = closestPoint(a1, b1, p, out.y, o);
                 return o;
         }
         else // Both u and u2 are out of bounds.
         {
        	 	Vector3f p;
                 if (out.x < 0.f)
                 {
                         p = a1;
                         out.x = 0.f;
                 }
                 else
                 {
                         p = b1;
                         out.x = 1.f;
                 }
 
                 Vector3f p2;
                 if (out.y < 0.f)
                 {
                         p2 = a2;
                         out.y = 0.f;
                 }
                 else
                 {
                         p2 = b2;
                         out.y = 1.f;
                 }
                 Vector2f v = new Vector2f(0,0);
                 Vector3f closestPoint = null;
                 v.x = closestPoint(a1, b1, p2, v.x, closestPoint);
                 Vector3f closestPoint2 = null;
                 v.y = closestPoint(a2, b2, p, v.y, closestPoint2);
                 
                 @SuppressWarnings("null")
				float dist1 = closestPoint.distance(p2);
                 @SuppressWarnings("null")
				float dist2 = closestPoint2.distance(p);
                 
                 if (dist1*dist1 <= dist2*dist2)
                 {
                         out.x = v.x;
                         return closestPoint;
                 }
                 else
                 {
                         out.y = v.x;
                         return p;
                 }
         }
 	}
	
	// Intersect segment S(t)=sa+t(sb-sa), 0<=t<=1 against cylinder specified by p, q and r
	static boolean intersectSegmentCylinder(Vector3f a, Vector3f b, Vector3f p, Vector3f q, float radius, Vector2f out)
	{
		Vector3f pq = q.sub(p), pa = a.sub(p), ab = b.sub(a);
		float md = pa.dot(pq);
		float nd = ab.dot(pq);
		float dd = pq.dot(pq);
		// Test if segment fully outside either endcap of cylinder
		if (md < 0.0f && md + nd < 0.0f) return false; // Segment outside ’p’ side of cylinder
		if (md > dd && md + nd > dd) return false; // Segment outside ’q’ side of cylinder
		float nn = ab.dot(ab);
		float mn = pa.dot(ab);
		float l = dd * nn - nd * nd;
		float k = pa.dot(pa) - radius * radius;
		float c = dd * k - md * md;
		if (Math.abs(l) < .001f) 
		{
			// Segment runs parallel to cylinder axis
			if (c > 0.0f) return false; // ’a’ and thus the segment lie outside cylinder
			// Now known that segment intersects cylinder; figure out how it intersects
			if (md < 0.0f) out.x = -mn / nn; // Intersect segment against ’p’ endcap
			else if (md > dd) out.x = (nd - mn) / nn; // Intersect segment against ’q’ endcap
			else out.x = 0.0f; // ’a’ lies inside cylinder
			return true;
		}
		float m = dd * mn - nd * md;
		float discr = m * m - l * c;
		if (discr < 0.0f) return false; // No real roots; no intersection
		out.x = ((-m - (float)Math.sqrt(discr)) / l);
		if (out.x < 0.0f || out.x > 1.0f) return false; // Intersection lies outside segment
		if (md + out.x * nd < 0.0f) 
		{
			// Intersection outside cylinder on ’p’ side
			if (nd <= 0.0f) return false; // Segment pointing away from endcap
			out.x = -md / nd;
			// Keep intersection if Dot(S(t) - p, S(t) - p) <= r^2
			return k + 2 * out.x * (mn + out.x * nn) <= 0.0f;
		} else if (md + out.x * nd > dd) 
		{
			// Intersection outside cylinder on ’q’ side
			if (nd >= 0.0f) return false; // Segment pointing away from endcap
			out.x = (dd - md) / nd;
			// Keep intersection if Dot(S(t) - q, S(t) - q) <= r^2
			return k + dd - 2 * md + out.x * (2 * (mn - nd) + out.x * nn) <= 0.0f;
		}
		// Segment intersects cylinder between the endcaps; t is correct
		return true;
	}
	
	static boolean intersectSegmentCapsule(Vector3f a, Vector3f b, Vector3f p, Vector3f q, float radius, Vector2f out)
	{
		Vector3f pq = q.sub(p), pa = a.sub(p), ab = b.sub(a);
		float md = pa.dot(pq);
		float nd = ab.dot(pq);
		float dd = pq.dot(pq);
		// Test if segment fully outside either endcap of cylinder
		if (md < 0.0f && md + nd < 0.0f) return false; // Segment outside ’p’ side of cylinder
		if (md > dd && md + nd > dd) return false; // Segment outside ’q’ side of cylinder
		float nn = ab.dot(ab);
		float mn = pa.dot(ab);
		float l = dd * nn - nd * nd;
		float k = pa.dot(pa) - radius * radius;
		float c = dd * k - md * md;
		if (Math.abs(l) < .001f) 
		{
			// Segment runs parallel to cylinder axis
			if (c > 0.0f) return false; // ’a’ and thus the segment lie outside cylinder
			// Now known that segment intersects cylinder; figure out how it intersects
			if (md < 0.0f) out.x = -mn / nn; // Intersect segment against ’p’ endcap
			else if (md > dd) out.x = (nd - mn) / nn; // Intersect segment against ’q’ endcap
			else out.x = 0.0f; // ’a’ lies inside cylinder
			return true;
		}
		float m = dd * mn - nd * md;
		float discr = m * m - l * c;
		if (discr < 0.0f) return false; // No real roots; no intersection
		out.x = ((-m - (float)Math.sqrt(discr)) / l);
		if (out.x < 0.0f || out.x > 1.0f) return false; // Intersection lies outside segment
		if (md + out.x * nd < 0.0f) 
		{
			// Intersection outside cylinder on ’p’ side
			if (nd <= 0.0f) return false; // Segment pointing away from endcap
			out.x = -md / nd;
			// Keep intersection if Dot(S(t) - p, S(t) - p) <= r^2
			return k + 2 * out.x * (mn + out.x * nn) <= 0.0f;
		} else if (md + out.x * nd > dd) 
		{
			// Intersection outside cylinder on ’q’ side
			if (nd >= 0.0f) return false; // Segment pointing away from endcap
			out.x = (dd - md) / nd;
			// Keep intersection if Dot(S(t) - q, S(t) - q) <= r^2
			return k + dd - 2 * md + out.x * (2 * (mn - nd) + out.x * nn) <= 0.0f;
		}
		// Segment intersects cylinder between the endcaps; t is correct
		return true;
	}
}
