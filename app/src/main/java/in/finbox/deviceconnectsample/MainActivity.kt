package `in`.finbox.deviceconnectsample

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import `in`.finbox.deviceconnectsample.databinding.ActivityMainBinding
import `in`.finbox.mobileriskmanager.FinBox
import `in`.finbox.mobileriskmanager.common.annotations.FinBoxErrorCode
import `in`.finbox.mobileriskmanager.devicematch.DeviceMatch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val TAG = "MainActivity"

    private val SMS_PERMISSION = Manifest.permission.READ_SMS

    /**
     * FinBox SDK instance
     */
    private val finBox = FinBox()

    /**
     * Activity Result Launcher
     */
    private val launcher =
        registerForActivityResult(
            RequestMultiplePermissions()
        ) { permissionMap: Map<String, Boolean> ->
            permissionsGranted(permissionMap)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initListeners()
    }

    private fun initListeners() {
        mBinding.requestPermissionButton.setOnClickListener {
            requestPermissions()
        }

        mBinding.createUserButton.setOnClickListener {
            createUser()
        }

        mBinding.startSyncButton.setOnClickListener {
            finBox.startPeriodicSync()
            disableSyncButton()
        }
    }

    private fun requestPermissions() {
        val permissions = getPermissionList(this)
            ?.filterNotNull()       // Remove any null entries
            ?.toTypedArray()        // Convert List<String> → Array<String>

        if (!permissions.isNullOrEmpty()) {
            launcher.launch(permissions)
        }
    }


    /**
     * Get the list of permissions that needs to be requested
     */
    private fun getPermissionList(context: Context): Array<String>? {
        try {
            // Get the current package information
            val packageInfo: PackageInfo =
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
                )
            return packageInfo.requestedPermissions
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package Not Found", e)
            return null
        }
    }

    private fun permissionsGranted(permissionMap: Map<String, Boolean>) {
        permissionMap.forEach { (key, value) ->
            Log.d(TAG, "$key Granted: $value")
        }

        // Condition 1: Check if ALL permissions are granted
        val allGranted = permissionMap.values.all { it }

        // Condition 2: Check if SMS permission alone is granted
        val smsGranted = permissionMap[SMS_PERMISSION] == true

        val enableCreateUser = allGranted || smsGranted

        mBinding.createUserButton.isEnabled = enableCreateUser
        mBinding.requestPermissionButton.isEnabled = !enableCreateUser

        Log.d(TAG, "permissionsGranted: createUserButton: $enableCreateUser")
    }

    private fun createUser() {
        val apiKey = "YOUR_API_KEY"

        val customerId = Utils.getCustomerId()

        Log.d(TAG, "createUser: CustomerId: $customerId")

        // Update the text view with customerId
        mBinding.customerIdTextView.text = customerId

        // Create the user or get a reference to an existing user
        FinBox.createUser(
            apiKey, customerId,
            object : FinBox.FinBoxAuthCallback {
                override fun onSuccess(accessToken: String) {
                    // Save user creation successful flag

                    // Set Device Match
                    finBox.setDeviceMatch(getDeviceMatch())

                    // Disable create user button
                    disableCreateUserButton()

                    // Enable sync buttons
                    enableSyncButton()

                    // Reset Error message
                    resetErrorMessage()
                }

                override fun onError(@FinBoxErrorCode errorCode: Int) {
                    // Authentication Failed
                    // Check the documentation to get better sense of error codes
                    mBinding.errorTextView.text = errorCode.toString()
                }
            })
    }

    private fun getDeviceMatch(): DeviceMatch {
        val builder = DeviceMatch.Builder()
        if (mBinding.emailTextInputEditText.getText() == null) {
            builder.setEmail(mBinding.emailTextInputEditText.getText().toString())
        }
        if (mBinding.nameTextInputEditText.getText() == null) {
            builder.setName(mBinding.nameTextInputEditText.getText().toString())
        }
        if (mBinding.phoneTextInputEditText.getText() == null) {
            builder.setPhone(mBinding.phoneTextInputEditText.getText().toString())
        }
        return builder.build()
    }

    /**
     * Enable the sync buttons
     */
    private fun enableSyncButton() {
        mBinding.startSyncButton.setEnabled(true)
    }

    /**
     * Reset the error message, if it is not empty
     */
    private fun resetErrorMessage() {
        if (mBinding.errorTextView.getText().length > 0) {
            // Reset the text
            mBinding.errorTextView.text = ""
        }
    }

    private fun disableCreateUserButton() {
        mBinding.createUserButton.isEnabled = false
    }

    private fun disableSyncButton() {
        mBinding.startSyncButton.isEnabled = false
    }
}