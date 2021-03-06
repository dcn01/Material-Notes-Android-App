package com.bijoysingh.quicknote.activities.sheets

import android.app.Dialog
import android.view.View
import android.widget.TextView
import com.bijoysingh.quicknote.R
import com.bijoysingh.quicknote.activities.MainActivity
import com.bijoysingh.quicknote.activities.ThemedActivity
import com.bijoysingh.quicknote.database.Note
import com.bijoysingh.quicknote.utils.NoteState


class AlertBottomSheet : ThemedBottomSheetFragment() {

  var listener: AlertDetails? = null
  override fun getBackgroundView(): Int {
    return R.id.container_layout
  }

  override fun setupView(dialog: Dialog?) {
    super.setupView(dialog)
    if (dialog == null) {
      return
    }

    val details = listener
    if (details === null) {
      return
    }

    val sheetTitle = dialog.findViewById<TextView>(R.id.alert_title)
    val sheetDescription = dialog.findViewById<TextView>(R.id.alert_description)
    val sheetYes = dialog.findViewById<TextView>(R.id.alert_yes)
    val sheetNo = dialog.findViewById<TextView>(R.id.alert_no)

    sheetTitle.setTextColor(getColor(R.color.dark_tertiary_text, R.color.light_secondary_text))
    sheetDescription.setTextColor(getColor(R.color.dark_tertiary_text, R.color.light_tertiary_text))
    sheetYes.setTextColor(getColor(R.color.colorAccent, R.color.colorAccentDark))
    sheetNo.setTextColor(getColor(R.color.dark_hint_text, R.color.light_tertiary_text))

    sheetTitle.setText(details.getTitle())
    sheetDescription.setText(details.getDescription())
    sheetYes.setText(details.getPositiveText())
    sheetNo.setText(details.getNegativeText())

    sheetYes.setOnClickListener {
      details.getPositiveClickListener()
      dismiss()
    }

    sheetNo.setOnClickListener {
      details.getNegativeClickListener()
      dismiss()
    }
  }

  override fun getLayout(): Int = R.layout.bottom_sheet_alert

  companion object {
    fun openSheet(activity: ThemedActivity, listener: AlertDetails) {
      val sheet = AlertBottomSheet()
      sheet.isNightMode = activity.isNightMode
      sheet.listener = listener
      sheet.show(activity.supportFragmentManager, sheet.tag)
    }

    fun openDeleteTrashSheet(activity: MainActivity) {
      val details = object: AlertDetails {
        override fun getTitle(): Int = R.string.delete_sheet_are_you_sure

        override fun getDescription(): Int = R.string.delete_sheet_delete_trash

        override fun getPositiveText(): Int = R.string.delete_sheet_delete_trash_yes

        override fun getNegativeText(): Int = R.string.delete_sheet_delete_trash_no

        override fun getPositiveClickListener() {
          val notes = Note.db(activity).getByNoteState(arrayOf(NoteState.TRASH.name))
          for (note in notes) {
            note.delete(activity)
          }
          activity.setupData()
        }

        override fun getNegativeClickListener() {
          // Ignore, nothing needs to happen
        }
      }
      openSheet(activity, details)
    }

    fun openDeleteNotePermanentlySheet(activity: MainActivity, note: Note) {
      val details = object: AlertDetails {
        override fun getTitle(): Int = R.string.delete_sheet_are_you_sure

        override fun getDescription(): Int = R.string.delete_sheet_delete_note_permanently

        override fun getPositiveText(): Int = R.string.delete_sheet_delete_trash_yes

        override fun getNegativeText(): Int = R.string.delete_sheet_delete_trash_no

        override fun getPositiveClickListener() {
          note.delete(activity)
          activity.setupData()
        }

        override fun getNegativeClickListener() {
          // Ignore, nothing needs to happen
        }
      }
      openSheet(activity, details)
    }
  }

  interface AlertDetails {
    fun getTitle(): Int

    fun getDescription(): Int

    fun getPositiveText(): Int

    fun getNegativeText(): Int

    fun getPositiveClickListener()

    fun getNegativeClickListener()
  }
}