package com.example.imagecapturemodule


import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.camera.core.Camera
import androidx.camera.core.ImageCapture
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.imagecapturemodule.databinding.FragmentQuestionBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class QuestionFragment : Fragment(), View.OnClickListener {

    val viewModel: ImageViewModel by activityViewModels {
        ImageViewModelFactory((requireActivity().application as ImageDetailsApplication).repository)

    }

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private var mSelectedOptionPosition: Int = 0
    private var mCurrentPosition: Int = 1
    private var mQuestionList: ArrayList<Question>? = null
    private var selected_option = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mQuestionList = Constants.getQuestion()
        setQuestion()



        binding.tvNext.setOnClickListener(this)

        binding.llOption1.setOnClickListener(this)
        binding.llOption2.setOnClickListener(this)
        binding.llOption3.setOnClickListener(this)
        binding.llOption4.setOnClickListener(this)
    }


    fun setQuestion() {
        requireActivity().startService(Intent("CAPTURE").apply {
            setClass(requireContext(), CameraXAsForegroundService::class.java)
        })

        val question = mQuestionList!![mCurrentPosition - 1]

        viewModel.current_ques = _binding?.tvQuestion?.text.toString()
        viewModel.current_ans = selected_option

        defaultOptionView()
        if (mCurrentPosition == mQuestionList!!.size) {
            binding.tvNext.text = "Finish"

        } else {
            binding.tvNext.text = "Next Question"
        }
        binding.tvQuestion.text = question!!.question
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour
        binding.imgQues.setImageResource(question.image)

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_option1 -> {
                selectedOptionView(binding.tvOptionOne, binding.llOption1, binding.img1, 1)
                selected_option = binding.tvOptionOne.text.toString()
            }
            R.id.ll_option2 -> {
                selectedOptionView(binding.tvOptionTwo, binding.llOption2, binding.img2, 2)
                selected_option = binding.tvOptionTwo.text.toString()
            }
            R.id.ll_option3 -> {
                selectedOptionView(binding.tvOptionThree, binding.llOption3, binding.img3, 3)
                selected_option = binding.tvOptionThree.text.toString()

            }
            R.id.ll_option4 -> {
                selectedOptionView(binding.tvOptionFour, binding.llOption4, binding.img4, 4)
                selected_option = binding.tvOptionFour.text.toString()
            }
            R.id.tv_next -> {

                /* requireActivity().startService(Intent("CAPTURE").apply {
                     setClass(requireContext(), CameraXAsForegroundService::class.java)
                 })
 */
                //add question details
                //   imageViewModel.insert()

                Log.d("QuestionFragment", binding.tvQuestion.text.toString())

                //  viewModel.insertQues(binding.tvQuestion.text.toString(), mCurrentPosition - 1)

                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++
                    when {
                        mCurrentPosition <= mQuestionList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            //  viewModel.submitAllData()
                            findNavController().navigate(R.id.action_questionFragment_to_detailsFragment)

                            //Toast.makeText(requireContext(), "last", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    /**/
                }
            }
        }
    }

    private fun defaultOptionView() {
        var options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)
        for (option in options) {
            option.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )

        }

        var imgoptions = ArrayList<ImageView>()
        imgoptions.add(0, binding.img1)
        imgoptions.add(1, binding.img2)
        imgoptions.add(2, binding.img3)
        imgoptions.add(3, binding.img4)
        for (option in imgoptions) {
            option.setBackgroundResource(R.drawable.ic_radio_button)

        }
        var lloptions = ArrayList<LinearLayout>()
        lloptions.add(0, binding.llOption1)
        lloptions.add(1, binding.llOption2)
        lloptions.add(2, binding.llOption3)
        lloptions.add(3, binding.llOption4)
        for (option in lloptions) {
            option.background =
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.grey_rounded_corner_with_stroke
                )

        }
    }

    private fun selectedOptionView(
        tv: TextView,
        ll: LinearLayout,
        im: ImageView,
        SelectedOptionNum: Int
    ) {
        defaultOptionView()
        mSelectedOptionPosition == SelectedOptionNum
        tv.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.orange
            )
        );
        ll.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_corner_orange_stroke)
        im.setBackgroundResource(R.drawable.ic_radio_button_fill)
    }
}