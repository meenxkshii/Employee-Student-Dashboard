import { useLocation, useNavigate } from "react-router-dom";
import "../styles/Student.css"; 

const Student = () => {
  const { state } = useLocation();
  const navigate = useNavigate();

  return (
    <div className="student-container">
      <h2>Student Dashboard</h2>
      <div className="student-details">
        <p><strong>Name:</strong> {state?.name}</p>
        <p><strong>Course:</strong> {state?.course}</p>
        <p><strong>Username:</strong> {state?.username}</p>
      </div>
      <button className="logout-button" onClick={() => navigate("/")}>
        Logout
      </button>
    </div>
  );
};

export default Student;
