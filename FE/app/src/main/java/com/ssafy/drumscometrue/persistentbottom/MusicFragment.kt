package com.ssafy.drumscometrue.persistentbottom

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ssafy.drumscometrue.R

class MusicFragment: Fragment(){
    private lateinit var findSongButton : Button;
    private lateinit var title : TextView;
    private val REQUEST_CODE_PERMISSION = 123
    private val audioPicker: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedFileUri = result.data?.data
                if (selectedFileUri != null) {
                    // Now you have the URI of the selected MP3 file
                    // Use a MediaPlayer or other media player library to play the audio
                    val mediaPlayer = MediaPlayer()
                    try {
                        mediaPlayer.setDataSource(requireContext(), selectedFileUri)
                        mediaPlayer.prepare()
                        mediaPlayer.start()

                        title.setText("음원이 재생중이에요")
                        findSongButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.btn_selector_gray))
                        findSongButton.setClickable(false);
                        // Set up OnCompletionListener to detect when audio playback is completed
                        mediaPlayer.setOnCompletionListener { mp ->
                            title.setText("어떤 곡으로 연습하시겠어요?")
                            findSongButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.btn_selector_main))
                            findSongButton.setClickable(true);
                        }

                    } catch (e: Exception) {
                        // Handle any exceptions that may occur during playback
                        Log.d("audioPicker", "audioPicker")
                    }
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        findSongButton = view.findViewById<Button>(R.id.findSongBtn)
        title = view.findViewById(R.id.musicFrTitle)

        // Add a click listener to the button
        findSongButton.setOnClickListener {
            openFilePicker()
        }
        return view;
    }

    private fun openFilePicker() {
        // Check if permission is granted
        val permissionCheck = ContextCompat.checkSelfPermission(
            requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, open file picker
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/*" // Limit selection to audio files
            audioPicker.launch(intent)
        } else {
            // Permission has not been granted, request it from the user
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now open the file picker
                openFilePicker()
            } else {
                // Permission denied, handle this scenario
                // You can show a message to the user explaining why you need the permission
                Toast.makeText(requireContext(), "권한을 거부했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}