package homework2;

import java.util.Scanner;

public class UI {

	private GradeSystem gradeSystem;
	private String fileName = "src/gradeinput.txt";
	private Scanner scanner = new Scanner(System.in);
	private String registeredID = null;
	private String title[] = { "lab1", "lab2", "lab3", "mid-term", "final exam" };
	
	public UI()
	{
		this.gradeSystem = new GradeSystem(fileName);
		this.promptID();
	}
	
	
	/* method  promptID  ----------------------------------------------------------------------------------                                                                                                    
	* 詢問使用者的id的介面
	*
	* @param N/A
	* @return N/A
	*
	* Pseudo code:
	* 1.詢問使用者id，如果對方選擇離開則結束 
	* 2.否則，確認對方的id是否有效
	* 3.若有效，註冊使用者id，並詢問使用者指令
	* 4.否則取消註冊使用者id並結束
    *
	* Time estimate : O (1)
	* Example: ui.promptID(962001044) 
	----------------------------------------------------------------------------------------------------------*/
	private void promptID()
	{
		while(true)
		{
			System.out.print("輸入ID或Q(結束使用) :");
			String id = this.scanner.next();
			if(id.equals("Q"))
			{
				this.showFinishMsg();
				this.registeredID = null;
				break;
			}
			else
			{
				if(checkID(id))
				{
					this.registeredID = id;
					this.showWelcomeMsg();
					this.promptCommand();
				}
				else
					System.out.println("不存在此ID");
			}
		}
	}
	
	private boolean checkID(String ID)
	{
		return gradeSystem.containsID(ID);
	}
	
	private void showFinishMsg()
	{
		System.out.print("結束了再見!");
	}
	
	private void showWelcomeMsg()
	{
		System.out.println("歡迎 " + gradeSystem.students.get(registeredID).name);
	}
	
	private void promptCommand()
	{
		Student s = gradeSystem.students.get(registeredID);
		
		while(true)
		{
			this.showMenu();
			String command = scanner.next();
			if(command.equals("G"))
				this.UIGrade(s);
			else if(command.equals("R"))
				this.UIRank(s);
			else if(command.equals("A"))
				this.UIAverage();
			else if(command.equals("W"))
				this.UIWeight();
			else if(command.equals("E"))
				return;
			else
				System.out.println("指令錯誤");
		}

	}
	
	private void showMenu()
	{
		System.out.println("輸入指令 : ");
		System.out.println("(G) 顯示成績 Grade ");
		System.out.println("(R) 顯示排名 Rank ");
		System.out.println("(A) 顯示平均 Average ");
		System.out.println("(W) 更新配分 Weight ");
		System.out.println("(E) 離開選單 Exit ");
	}
	
	private void UIGrade(Student s)
	{
		System.out.println(s.name + "的成績");
		for(int i=0; i<title.length; i++)
		{
			System.out.print(title[i] + " : " + s.grades[i]);
			if(s.grades[i] < 60)
				System.out.println("*");
			else
				System.out.println();
		}
		System.out.print("total grade : " + s.totalGrade);
		if(s.totalGrade < 60)
			System.out.println("*");
		else
			System.out.println();
	}
	
	private void UIRank(Student s)
	{
		int rank = gradeSystem.showRank(registeredID);
		if(rank == -1)
			System.out.println("排名有問題");
		else
			System.out.println(s.name + "的排名是" + rank);
	}
	
	private void UIAverage()
	{
		float[] avg = gradeSystem.showAverage();
		System.out.println("平均分");
		for(int i=0; i<title.length; i++)
			System.out.println(title[i] + " : " + avg[i]);
		System.out.println("total grade : " + avg[title.length]);
	}
	
	private void UIWeight()
	{
		float[] newWeights = new float[gradeSystem.numofGrades];		
		System.out.println("舊配分");
		for(int i=0; i<title.length; i++)
			System.out.println(title[i] + " : " + gradeSystem.weights[i]);
		System.out.println("輸入新配分");
		for(int i=0; i<title.length; i++)
		{
			System.out.print(title[i]);
			newWeights[i] = Float.parseFloat(scanner.next());
		}
		this.gradeSystem.updateWeights(newWeights);
	}
}
