package labo1.Script_de_lecture;


public class ImportPrecedures {
    public static void main(String args[]) {
		
		LectureBD lecture = new LectureBD();
        int[] results = null;
        //lecture.createProcedures();
        lecture.importSqlFile("src/labo1/Script_de_lecture/creationView.sql");

		try {
            results = lecture.getStatement().executeBatch();
            lecture.getConnection().commit();
         } catch (Exception e) {
            System.err.println(e.getMessage());
         }
         lecture.closeConnection();
	}
}
