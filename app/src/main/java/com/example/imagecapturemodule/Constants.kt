package com.example.imagecapturemodule

object Constants {
    fun getQuestion(): ArrayList<Question> {
        val questionList = ArrayList<Question>()
        val ques1 = Question(
            "Your friend lent you a book which you have lost. Your mother reminds you about it. You will…",
            R.drawable.illustration,
            "It’s difficult to find it now since you have moved",
            "You do not remember getting any book",
            "You donated it to a poor student ",
            "Another friend lost the book "
        )
        questionList.add(ques1)
        val ques2 = Question(
            "As the Indian cricket team selector you selected a friend who falls marginally short of merit. Someone lodged a complaint against you. You will…",
            R.drawable.ic_illustration_1,
            "Ignore the matter & stay unbothered despite allegations",
            "Try to find loopholes in the complaint application",
            "Offer clarifications on why you selected him",
            "Indicate it's the committee's decision and not yours"
        )
        questionList.add(ques2)
        val ques3 = Question(
            "Your father caught you watching an obscene photo-graph in your mobile phone. He did not ask anything. You will say that…",
            R.drawable.ic_illustration_3,
            "You hate such photographs ",
            "Someone sent it to you as a prank",
            "Ask him as to why has he invaded your privacy ",
            "You are an adult and are not ashamed of the act"
        )
        questionList.add(ques3)
        val ques4 = Question(
            "Despite promising, you fail to give lift to your friend because you are getting late for a party.  Later you will tell them that…",
            R.drawable.ic_illustration_4,
            "Your boss gave you a task at the last moment",
            "Your mother was sick ",
            "You have not made any promises to them",
            "This is a small issue and they should ignore it"
        )
        questionList.add(ques4)
        return questionList
    }
}