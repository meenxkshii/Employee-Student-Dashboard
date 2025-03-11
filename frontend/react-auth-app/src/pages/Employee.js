import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaTrash } from "react-icons/fa";
import Swal from "sweetalert2";
import { apiClient } from "../axiosconfig.js";
import "../styles/Employee.css"; 
import "../styles/sweetalert.css"; 

const Employee = () => {
  const [students, setStudents] = useState([]);
  const navigate = useNavigate();

  
  useEffect(() => {
    apiClient
      .get("/students")
      .then((response) => setStudents(response.data))
      .catch((error) => console.error("Error fetching students:", error));
  }, []);

  const registerStudent = async () => {
    const { value: formValues } = await Swal.fire({
      title: "Register Student",
      html: `
        <input id="swal-input-name" class="swal2-input" placeholder="Student Name">
        <input id="swal-input-course" class="swal2-input" placeholder="Course Name">
      `,
      showCancelButton: true,
      confirmButtonText: "Register",
      cancelButtonText: "Cancel",
      allowOutsideClick: false,
      focusConfirm: false,
      customClass: {
        popup: "swal-popup",
        confirmButton: "swal-register-btn",
        cancelButton: "swal-cancel-btn",
      },
      preConfirm: () => {
        const name = document.getElementById("swal-input-name").value.trim();
        const course = document.getElementById("swal-input-course").value.trim();
  
        if (!name || !course) {
          Swal.showValidationMessage("Please enter both Name and Course!");
          return false;
        }
  
        if (name.length < 5) {
          Swal.showValidationMessage("Name must be at least 5 characters long!");
          return false;
        }
  
        return { name, course };
      },
    });
  
    if (formValues) {
      try {
        const response = await apiClient.post("/students/register", formValues);
        Swal.fire({
          title: "Student Registered!",
          text: `Username: ${response.data.username}`,
          icon: "success",
        });
  
        setStudents([...students, response.data]);
      } catch (error) {
        Swal.fire("Error", "Failed to register student", "error");
      }
    }
  };
  const deleteStudent = async (id) => {
    const confirmDelete = await Swal.fire({
      title: "Are you sure?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "Cancel",
      customClass: {
        confirmButton: "swal-confirm-btn",
        cancelButton: "swal-cancel-btn",
      },
    });
  
    if (confirmDelete.isConfirmed) {
      try {
        await apiClient.delete(`/students/${id}`);
        setStudents(students.filter((student) => student.id !== id));
        Swal.fire("Deleted!", "Student has been removed.", "success");
      } catch (error) {
        Swal.fire("Error", "Failed to delete student", "error");
      }
    }
  };
  

  const updateStudent = async (student) => {
    const { value: formValues } = await Swal.fire({
      title: "Update Student Details",
      html: `
        <input id="swal-input-name" class="swal2-input" value="${student.name}" placeholder="Student Name">
        <input id="swal-input-course" class="swal2-input" value="${student.course}" placeholder="Course Name">
      `,
      showCancelButton: true,
      confirmButtonText: "Update",
      cancelButtonText: "Cancel",
      allowOutsideClick: false,
      focusConfirm: false,
      customClass: {
        popup: "swal-popup",
        confirmButton: "swal-update-btn",
        cancelButton: "swal-cancel-btn",
      },
      preConfirm: () => {
        const name = document.getElementById("swal-input-name").value.trim();
        const course = document.getElementById("swal-input-course").value.trim();
  
        if (!name || !course) {
          Swal.showValidationMessage("Please enter both Name and Course!");
          return false;
        }
  
        return { id: student.id, name, course };
      },
    });
  
    if (formValues) {
      try {
        const response = await apiClient.put(`/students/${formValues.id}`, formValues);
        setStudents(
          students.map((s) =>
            s.id === formValues.id ? { ...s, ...response.data } : s
          )
        );
        Swal.fire("Updated!", "Student details have been updated.", "success");
      } catch (error) {
        Swal.fire("Error", "Failed to update student details", "error");
      }
    }
  };
  
  
  
  return (
    <div className="employee-container">
      <h1>Employee Dashboard</h1>

    
      <button onClick={registerStudent} className="register-btn">
        Register Student
      </button>

    
      <table className="student-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Course</th>
            <th>Username</th>
            <th>Password</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {students.map((student, index) => (
            <tr key={index}>
              <td>{student.name}</td>
              <td>{student.course}</td>
              <td>{student.username}</td>
              <td>{student.password}</td>
              <td>
                <button onClick={() => updateStudent(student)} className="icon-btn">
                  <FaEdit />
                </button>
                <button onClick={() => deleteStudent(student.id)} className="icon-btn">
                  <FaTrash />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

    
      <button
        onClick={() => {
          localStorage.removeItem("user");
          navigate("/");
        }}
        className="logout-btn"
      >
        Logout
      </button>
    </div>
  );
};

export default Employee;
