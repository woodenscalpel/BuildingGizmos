package com.woodenscalpel.buildinggizmos.misc.CatenaryHelper;

import net.minecraft.util.Tuple;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CatHelper {
    //Equation of caternary given A,B,C and X
    public double f(double a, double b, double c, double x) {
        return a*Math.cosh((x-c)/a)+b;
    }
    //Equation that needs to be solved for a
    double fa(double x1, double y1, double x2, double y2, double s, double a) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return 2*a*Math.sinh(dx/(2*a)) - Math.sqrt(s*s - dy*dy);
    }
    double fap(double x1, double y1, double x2, double y2, double s, double a) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return (2*Math.sinh(dx/(2*a)))-(dx*Math.cosh(dx/(2*a))/(a));
    }
    //Equation to solve for c
    double fc(double x1, double y1, double x2, double y2, double a, double c) {
        return a*Math.cosh((x2-c)/a)+y1-a*Math.cosh((x1-c)/a) - y2;
    }
    double fcp(double x1, double y1, double x2, double y2, double a, double c) {
        return Math.sinh((x1-c)/a)-Math.sinh((x2-c)/a);
    }
    //solve for b
    public double fb(double x1, double y1, double a, double c) {
        return y1 - a*Math.cosh((x1-c)/a);
    }

    public double newtona(double x1, double y1,double x2,double y2,double s) {
        double a1 = 999;
        //double a2 = 0.2;
        double a2 = findnewtoninit(x1,y1,x2,y2,s,0.2);
        double dela = a2-a1;
        double tolerance = 0.00000001;
        while(Math.abs(dela) > tolerance) {
            a1 = a2;
            a2 = a2 - fa(x1,y1,x2,y2,s,a2)/fap(x1,y1,x2,y2,s,a2);
            dela = a2 - a1;
        }
        return a2;
    }
    public double findnewtoninit(double x1, double y1,double x2,double y2,double s,double a) {
        if (fa(x1,y1,x2,y2,s,a*10) > 0) {
            return findnewtoninit(x1,y1,x2,y2,s,a*10);
        }
        return a;

    }
    public double newtonc(double x1, double y1,double x2,double y2,double a) {
        double c1 = 999;
        double c2 = 5;
        double delc = c2-c1;
        double tolerance = 0.000001;
        while(Math.abs(delc) > tolerance) {
            c1 = c2;
            c2 = c2 - fc(x1,y1,x2,y2,a,c2)/fcp(x1,y1,x2,y2,a,c2);
            delc = c2 - c1;
        }
        return c2;
    }

    public double magnitude(double... args) {
        double acu = 0;
        for (double i : args)
            acu += i*i;
        acu = Math.sqrt(acu);
        return acu;
    }

    public double dotprod(double x1,double z1, double x2, double z2) {
        return x1*x2+z1*z2;
    }

    public double theta2(double x1,double z1,double x2,double z2) {
        return Math.acos(((dotprod(x1,z1,x2,z2))/(magnitude(x1,z1)*magnitude(x2,z2))));
    }

    public double degtorad(double deg) {
        return deg*Math.PI/180.0;
    }

    public double radtodeg(double rad) {
        return rad*180.0/Math.PI;
    }

    public Tuple<Double,Double> yrotate(double x, double z, double t) {

        double x2 = x*Math.cos(t) + z*Math.sin(t);
        double z2 = -x*Math.sin(t) + z*Math.cos(t);
        return new Tuple<Double,Double>(x2,z2);
    }



    public List<Vec3> getCatPoints(Vec3 p1, Vec3 _p2, double s){
        double x1 = p1.x;
        double y1 = p1.y;
        double z1 = p1.z;
        double x2 = _p2.x;
        double y2 = _p2.y;
        double z2 = _p2.z;
        //Translate P2 in referance to P1
        double x2t = x2 - x1;
        double y2t = y2 - y1;
        double z2t = z2 - z1;
        //Rotate P2 into the same plane as P1

        double angle = theta2(1,0,x2t,z2t);

        if(z2t < 0) {
            angle = -angle;
        }
        Tuple p2 = yrotate(x2t, z2t, angle);
        double x2r = (double) p2.getA();
        double z2r = (double) p2.getB();
        //Find Cat A,B,C
        //assuming z2 = 0 from rotation
        double x1r = 0;
        double y1r = y1;
        double z1r = 0;

        // SWAP values if wrong

        if(x1r>x2r) {
            double tempx = x1r;
            double tempz = z2r;
            double tempy = y1;
            x1r = x2r;
            z1r = z2r;
            y1 = y2;
            y2 = tempy;
            x2r = tempx;
            z2r = tempz;
        }



        double a = newtona(x1r, y1, x2r, y2, s);
        double c = newtonc(x1r, y1, x2r, y2, a);
        double b = fb(x1r, y1, a, c);
        //Generate N points for Cat
        List<Vec3> points = new ArrayList<>();

        for(double xi = x1r ; xi < x2r; xi = xi + (x2r-x1r)/(10*s)) {
            //Texturizer.logger.info("Debug2 {} {}",xi,x2r);
            double yi = f(a, b, c, xi);
            double zi = 0;


            //Rotate all points back
            Tuple rotated = yrotate(xi, zi, -angle);
            double xir = (double) rotated.getA();
            double zir = (double) rotated.getB();
            //Add (Translated) Points to list
            // centering offset
            double centering = 0.5;
            Double xt = xir + x1 + centering;
            Double yt = yi + centering;
            Double zt = zir + z1 + centering;
            points.add(new Vec3(xt,yt,zt));
        }
        return points;
    }

}



