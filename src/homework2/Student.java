package homework2;



public class Student {
	
	public String id;
	public String name;
	public int[] grades;
	public int totalGrade;
	
	public Student(String id, String name, int[] grades)
	{
		this.id = id;
		this.name = name;
		this.grades = grades;
	}
	
	
	/* method  calculateTotalGrade  ----------------------------------------------------------------------------------                                                                                                    
	* 依照加權配分計算單一學生的總成績
	*
	* @param weights  設定好的加權
	* @return N/A
	*
	* Pseudo code:
	* 1.將各項分數乘以相對應的加權，然後加總，最後除以考試的數目，即為總成績 
	* 2.設定新的總成績
    *
	* Time estimate : O (m)
	* Example: student.calculateTotalGrade(weights) ; weighs = { 0.2, 0.2, 0.2, 0.2, 0.2}  
	----------------------------------------------------------------------------------------------------------*/
	public void calculateTotalGrade(float[] weights)
	{
		float sum = 0;
		for(int i=0; i<grades.length; i++)
		{
			sum += grades[i] * weights[i];
		}
		this.totalGrade = (int) Math.round(sum);
	}
}

