package com.siheung_alba.alba.model

import android.os.Parcel
import android.os.Parcelable

class ResumeModel : Parcelable {
    val email: String?
    val title: String?
    val career: String?
    val introduce: String?
    val resume_id: String?
    val created_at: String?
    val updated_at: String?

    constructor(
        email: String?,
        title: String?,
        career: String?,
        introduce: String?,
        resume_id: String?,
        created_at: String?,
        updated_at: String?
    ) {
        this.email = email
        this.title = title
        this.career = career
        this.introduce = introduce
        this.resume_id = resume_id
        this.created_at = created_at
        this.updated_at = updated_at
    }

    constructor(parcel: Parcel) {
        email = parcel.readString()
        title = parcel.readString()
        career = parcel.readString()
        introduce = parcel.readString()
        resume_id = parcel.readString()
        created_at = parcel.readString()
        updated_at = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(title)
        parcel.writeString(career)
        parcel.writeString(introduce)
        parcel.writeString(resume_id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResumeModel> {
        override fun createFromParcel(parcel: Parcel): ResumeModel {
            return ResumeModel(parcel)
        }

        override fun newArray(size: Int): Array<ResumeModel?> {
            return arrayOfNulls(size)
        }
    }
}
