package com.example;




public class ExampleDaoGenerator {
    public static final String DAO_PATH = "C:/Users/LYW/AndroidStudioProjects/M" +
            "aoMaoRobot/app/src/main/java-gen";
    public static final String PACKAGE_NAME = "com.lyw.maomaorobot.greendao";

    public static void main(String[] args)throws Exception{
        Schema schema = new Schema(1,PACKAGE_NAME);
        addNote(schema);
        new DaoGenerator().generateAll(schema,DAO_PATH);
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");
        note.addIdProperty().primaryKey().autoincrement();
        note.addIntProperty("plus");
        note.addStringProperty("tips");
    }

}
