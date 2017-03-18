package homework2;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GradeSystem {
	
	public HashMap<String, Student> students;
	
	public static int numofGrades = 5;
	public float[] weights = { 0.1f, 0.1f, 0.1f, 0.3f, 0.4f };
	
	private String title[] = { "lab1", "lab2", "lab3", "mid-term", "final exam" };
	private ArrayList<Student> sorted;
	
	public GradeSystem(String fileName)
	{
		this.students = new HashMap<String, Student>();
		this.readFile(fileName);
		this.updateWeights(weights);
	}
	
	
	/* method  containsID  ----------------------------------------------------------------------------------                                                                                                    
	* 用來確認欲查詢的ID是否存在於GradeSystems系統內
	*
	* @param ID     用來查詢的ID
	* @return 一個布林值，若為true則代表GradeSystems有這筆資料，若false則否
	*
	* Time estimate : O (n)
	* Example: gradeSystem.checkID(962001044) ; 傳回結果為 true
	----------------------------------------------------------------------------------------------------------*/
	public boolean containsID(String ID)
	{
		return students.containsKey(ID);
	}
	
	
	/* method  showRank  ----------------------------------------------------------------------------------                                                                                                    
	* 將在GradeSystems系統內的所有學生依照總成績由高至低排序，並回傳該ID學生的排名
	*
	* @param ID     用來查詢的ID
	* @return 一個整數，代表該學生在所有學生中的排名
	*
	* Pseudo code:
	* 1.從排好名次的ArrayList sorted中 找出id和要查詢的id相同
	* 2.回傳找到的index，加1即為名次
	* 3.若回傳-1，表示在排序的過程中發生意外
    *
	* Time estimate : O (n)
	* Example: gradeSystem.showRank(962001044) ; 傳回結果為 19
	----------------------------------------------------------------------------------------------------------*/
	public int showRank(String ID)
	{   
		for(Student sort : sorted)
			if(sort.id.equals(ID))
				return sorted.indexOf(sort)+1;
		return -1;
	}
	
	
	/* method  updateWeights  ----------------------------------------------------------------------------------                                                                                                    
	* 用來更新加權配分、更新總成績、以及重新排序
	*
	* @param weight  欲設定的新加權
	* @return N/A
	*
	* Pseudo code:
	* 1.設定新加權 
	* 2.更新所有人的總成績，總共需要花費 學生人數(n) * 考試次數(m) 的時間
	* 3.依照所有人的總成績做排序
    *
	* Time estimate : O(m) + O(m*n) + O (nlg(n)) = O (nlg(n))  
	* m 為考試次數， n為學生人數 ，假設 m << n
	* Example: gradeSystem.updateWeights(weights) ; weights = { 0.2, 0.2, 0.2, 0.2, 0.2}  
	----------------------------------------------------------------------------------------------------------*/
	public void updateWeights(float[] weights)
	{
		this.weights = weights;
		this.updateTotalScore();
		this.sorted = sort(); 
	}
	
	
	/* method  showAverage  ----------------------------------------------------------------------------------                                                                                                    
	* 回傳各次考試以及總成績全部學生的平均分
	*
	* @param ID  N/A
	* @return 一個大小為6的float array，包含各次考試以及總成績的平均分
	*
	* Pseudo code:
	* 1.scan所有學生的成績，將學生的每次考試成績分別加到一個avg的array，avg用來儲存單次考試成績的所有學生的總分
	* 2.scan完所有成績以後，將各次考試的總分除以學生人數是各次考試的平均分
	* 3.累加單次考試的總分，並作加權，最後除以學生人數則是總平均
	* 3.回傳所有算出來的平均分
    *
	* Time estimate : O (m*n)
	* Example: gradeSystem.showAverage() ; 回傳float陣列
	----------------------------------------------------------------------------------------------------------*/
	public float[] showAverage()
	{
		float[] avg = new float[numofGrades+1];
		for(Map.Entry<String, Student> entry : students.entrySet()) {
			Student s = entry.getValue();
			for(int i=0; i<numofGrades; i++)
				avg[i] += s.grades[i];
		}
		for(int i=0; i<numofGrades; i++)
		{
			avg[numofGrades] += avg[i] * weights[i];	// total grade
			avg[i] /= students.size();
		}
		avg[numofGrades] /= students.size();
		return avg;
	}

	
	/* method  readFile  ----------------------------------------------------------------------------------                                                                                                    
	* 讀取input格式，並且create Student object
	*
	* @param fileName  input file的相對路徑
	* @return N/A
	*
	* Pseudo code:
	* 1.使用reader一行一行的讀檔
	* 2.將獨到的每一行用空白切開，切出來的第一個token是id，第二個是名字，接下來的tokens轉成整數型態後，依序塞進成績的陣列
	* 3.create Student object，並依照初始加權計算總分
	* 4.將Student object塞進hash map裡，key是id，value則是Student object
    *
	* Time estimate : O (m*n)
	* Example: gradeSystem.readFile("input.txt") 
	----------------------------------------------------------------------------------------------------------*/
	private void readFile(String fileName)
	{
		try {
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			while ((sCurrentLine = br.readLine()) != null) {
				// format : id name lab1 lab2 lab3 mid final
				// 955002056 許文馨 88 92 88 98 91
				String[] tokens = sCurrentLine.split(" ");
				String id = tokens[0], name = tokens[1];
				int[] grades = new int[numofGrades];
				for(int i=0; i<grades.length; i++)
					grades[i] = Integer.parseInt(tokens[2+i]);

				Student s = new Student(id, name, grades);
				s.calculateTotalGrade(weights);
				this.students.put(id, s);
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void updateTotalScore()
	{
		for(Map.Entry<String, Student> entry : students.entrySet()) {
			Student s = entry.getValue();
			s.calculateTotalGrade(weights);
		}
	}
	
	private ArrayList<Student> sort()
	{
		ArrayList<Student> s = new ArrayList<Student>(students.values());
		Comparator<Student> sortByValue = (e1, e2)->{ return e1.totalGrade - e2.totalGrade; };
		s.sort(sortByValue);
		return s;
	}
}

