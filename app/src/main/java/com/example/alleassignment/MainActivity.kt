package com.example.alleassignment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.ContentUris
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentResolverCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.alleassignment.data.model.Image
import com.example.alleassignment.databinding.ActivityMainBinding
import com.example.alleassignment.ui.fragment.ScreenshotDetailsFragment
import com.example.alleassignment.ui.fragment.ScreenshotsFragment
import com.example.alleassignment.ui.viewmodel.ScreenshotsViewModel
import com.example.alleassignment.util.gone
import com.example.alleassignment.util.openPermissionSettings
import com.example.alleassignment.util.show
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: ScreenshotsViewModel by viewModels()

    private var storagePermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) READ_MEDIA_IMAGES
        else READ_EXTERNAL_STORAGE

    private val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    private val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.RELATIVE_PATH
    )
    private val selection = "${MediaStore.Video.Media.RELATIVE_PATH} = ?"
    private val selectionArgs = arrayOf("DCIM/Screenshots/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavViewHome.setOnItemSelectedListener {
            if (it.itemId == binding.bottomNavViewHome.selectedItemId) return@setOnItemSelectedListener true
            if (it.itemId == R.id.actionScreenshots) {
                supportFragmentManager.popBackStack()
            } else {
                supportFragmentManager.commit {
                    addToBackStack(null)
                    replace(binding.fcvHome.id, ScreenshotDetailsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
        binding.btnAskPermission.setOnClickListener {
            checkForMediaPermission()
        }
        checkForMediaPermission(false)
    }

    private fun checkForMediaPermission(openSettings: Boolean = true) {
        binding.groupLoader.show()
        binding.groupPermission.gone()
        binding.groupHomeScreen.gone()
        if (ContextCompat.checkSelfPermission(this, storagePermission) == PERMISSION_GRANTED) {
            queryImages()
        } else if (Prefs.getBoolean("permission_denied_forever")) {
            if (openSettings) {
                openPermissionSettings(settingsLauncher)
            } else {
                binding.groupLoader.gone()
                binding.groupPermission.show()
                binding.groupHomeScreen.gone()
            }
        } else {
            requestPermissionLauncher.launch(storagePermission)
        }
    }

    private fun queryImages() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val query = ContentResolverCompat.query(
                    contentResolver,
                    collection,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null
                )
                var position = 0
                query?.use { cursor ->
                    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                    val relativePathColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)
                    while (cursor.moveToNext()) {
                        val id = cursor.getLong(idColumn)
                        val name = cursor.getString(nameColumn)
                        val size = cursor.getInt(sizeColumn)
                        val relativePath = cursor.getString(relativePathColumn)
                        val contentUri: Uri = ContentUris.withAppendedId(collection, id)
                        viewModel.imageList += Image(
                            contentUri,
                            name,
                            size,
                            relativePath,
                            position
                        )
                        position++
                    }
                }
                // for demo purposes, purposely added a delay to show syncing screenshots loading state
                // remove this delay in production app
                delay(400)
            }
            binding.groupLoader.gone()
            if (viewModel.imageList.isNotEmpty()) {
                viewModel.selectedImage = viewModel.imageList[0]
                binding.groupHomeScreen.show()
                supportFragmentManager.commit {
                    add(binding.fcvHome.id, ScreenshotsFragment())
                }
            } else {
                binding.tvNoScreenshots.show()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                queryImages()
            } else {
                val openSettings = !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    storagePermission
                )
                Prefs.putBoolean("permission_denied_forever", openSettings)
                binding.groupLoader.gone()
                binding.groupPermission.show()
            }
        }

    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (ContextCompat.checkSelfPermission(this, storagePermission) == PERMISSION_GRANTED) {
                queryImages()
            } else {
                binding.groupLoader.gone()
                binding.groupPermission.show()
            }
        }

    override fun onBackPressed() {
        if (binding.bottomNavViewHome.selectedItemId == R.id.actionScreenshotDetails) {
            binding.bottomNavViewHome.selectedItemId = R.id.actionScreenshots
        } else super.onBackPressed()
    }
}