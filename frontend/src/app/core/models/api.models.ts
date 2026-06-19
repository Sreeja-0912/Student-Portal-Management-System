export type Role = 'ADMIN' | 'FACULTY' | 'STUDENT';

export interface AuthResponse { token: string; username: string; email: string; role: Role; }
export interface LoginRequest { username: string; password: string; }
export interface RegisterRequest { username: string; email: string; password: string; role: Role; }
export interface Page<T> { content: T[]; totalElements: number; totalPages: number; size: number; number: number; }

export interface Student { id: number; rollNumber: string; firstName: string; lastName?: string; email: string; phone?: string; department?: string; semester?: number; userId?: number; }
export interface Course { id: number; courseCode: string; courseName: string; credits: number; facultyName?: string; }
export interface Enrollment { id: number; studentId: number; studentName: string; courseId: number; courseCode: string; courseName: string; }
export interface Attendance { id: number; studentId: number; studentName: string; courseId: number; courseCode: string; courseName: string; attendancePercentage: number; }
export interface Marks { id: number; studentId: number; studentName: string; courseId: number; courseCode: string; courseName: string; score: number; grade: string; result: 'PASS' | 'FAIL'; }
export interface Ranking { rank: number; studentId: number; studentName: string; rollNumber: string; department: string; averageScore: number; grade: string; }
export interface Announcement { id: number; title: string; description: string; createdByName: string; createdDate: string; }
export interface StudyMaterial { id: number; courseId: number; courseCode: string; courseName: string; title: string; fileUrl: string; }
export interface Summary { totalStudents: number; totalCourses: number; averageAttendance: number; passPercentage: number; }
export interface BandStatistic { label: string; count: number; }
export interface AtRiskStudent { studentId: number; studentName: string; rollNumber: string; averageAttendance: number; averageScore: number; reason: string; }
export interface CoursePerformance { courseId: number; courseCode: string; courseName: string; averageScore: number; highestScore: number; lowestScore: number; }
export interface DepartmentPerformance { department: string; averageScore: number; }
export interface PassFailStatistics { passCount: number; failCount: number; passPercentage: number; failPercentage: number; }
export interface MonthlyEnrollment { month: number; count: number; }
export interface AuditLog { id: number; action: string; username: string; createdDate: string; }
