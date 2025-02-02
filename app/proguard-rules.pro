# Keep all @Serializable classes (including your CurrencyType enum)
-keep,allowobfuscation @kotlinx.serialization.Serializable class * { *; }

# Keep generated serializer classes (important for Enums)
-keep class **$$serializer { *; }

# Keep enum serialization
-keep class kotlinx.serialization.internal.EnumSerializer { *; }

# Ensure Kotlin metadata annotations are retained
-keepattributes *Annotation*

# Keep classes that extend KSerializer
-keep class * extends kotlinx.serialization.KSerializer { *; }

# Keep all enums' names and methods intact (including values() and valueOf())
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * extends java.lang.Enum { *; }