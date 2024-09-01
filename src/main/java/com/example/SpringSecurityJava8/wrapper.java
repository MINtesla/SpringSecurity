package com.example.SpringSecurityJava8;

public class wrapper {
    public static void main(String[] args){
        Boolean b = true;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=true;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        b=false;
        System.out.println(b+"--"+b.hashCode()+";"+System.identityHashCode(b));
        Boolean c = false;
        System.out.println(b==c);
    }
}
