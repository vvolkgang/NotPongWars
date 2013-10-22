package com.notpongwars.core;

import android.graphics.PointF;

public class MathHelper {
	
	public static final PointF PointOne = new PointF(1,1);
	
	/*
    public static PointF Normalize(PointF value)
    {
      float num = 1f / (float) Math.sqrt((double) value.x * (double) value.x + (double) value.y * (double) value.y);
      PointF point = new Pointf ;
      point.x = value.x * num;
      point.y = value.y * num;
      return new PointF(value.x * num, value.y * num);
    }
    */
    public static void NormalizeByRef(PointF point)
    {
      float num = 1f / (float) Math.sqrt((double) point.x * (double) point.x + (double) point.y * (double) point.y);  
      point.set(point.x * num, point.y * num);
    }
    
    public static PointF Normalize(PointF point)
    {
      float num = 1f / (float) Math.sqrt((double) point.x * (double) point.x + (double) point.y * (double) point.y);  
      return new PointF(point.x * num, point.y * num);
    }
    
    public static void AddToPoint1ByRef(PointF point1, PointF point2){
    	point1.set(point1.x + point2.x, point1.y + point2.y);
    }
    
}


