-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 24, 2024 at 07:06 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `timetable`
--

-- --------------------------------------------------------

--
-- Table structure for table `listofclass`
--

CREATE TABLE `listofclass` (
  `Class_id` int(11) NOT NULL,
  `Class_name` varchar(255) NOT NULL,
  `Class_inchargename` varchar(255) NOT NULL,
  `TotalStudent` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `listofclass`
--

INSERT INTO `listofclass` (`Class_id`, `Class_name`, `Class_inchargename`, `TotalStudent`) VALUES
(3, 'CSE F', 'Raghavan', 78),
(4, 'CSE D', 'Raghavan', 23),
(6, 'CSE E 3', 'Somesh', 70);

-- --------------------------------------------------------

--
-- Table structure for table `professors`
--

CREATE TABLE `professors` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `joining_date` date NOT NULL,
  `is_class_incharge` tinyint(1) DEFAULT 0,
  `class_incharge_subject` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `class_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `professors`
--

INSERT INTO `professors` (`id`, `name`, `position`, `joining_date`, `is_class_incharge`, `class_incharge_subject`, `created_at`, `updated_at`, `class_name`) VALUES
(2, 'Raghavan', 'CSE-HOD', '2003-02-12', 0, NULL, '2024-11-24 12:48:59', '2024-11-24 17:47:05', NULL),
(3, 'Somesh', 'MECH-HOD', '2005-11-14', 0, NULL, '2024-11-24 17:48:01', '2024-11-24 17:48:01', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `professor_subjects`
--

CREATE TABLE `professor_subjects` (
  `professor_id` int(11) NOT NULL,
  `subject_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `professor_subjects`
--

INSERT INTO `professor_subjects` (`professor_id`, `subject_name`) VALUES
(2, 'DAA'),
(2, 'OPPS'),
(2, 'CA'),
(3, 'DBMS'),
(3, 'PRP');

-- --------------------------------------------------------

--
-- Table structure for table `roomlist`
--

CREATE TABLE `roomlist` (
  `Room_id` int(11) NOT NULL,
  `Room_Name` varchar(255) NOT NULL,
  `Room_Capacity` int(11) NOT NULL,
  `Smartboard` tinyint(1) NOT NULL,
  `Spacker` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `roomlist`
--

INSERT INTO `roomlist` (`Room_id`, `Room_Name`, `Room_Capacity`, `Smartboard`, `Spacker`) VALUES
(1, 'A102', 23, 1, 1),
(2, 'A213', 34, 1, 0),
(3, 'A201', 78, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `timetable`
--

CREATE TABLE `timetable` (
  `room` varchar(50) NOT NULL,
  `day` int(11) NOT NULL,
  `period` int(11) NOT NULL,
  `professor` varchar(100) DEFAULT NULL,
  `class` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `timetable`
--

INSERT INTO `timetable` (`room`, `day`, `period`, `professor`, `class`) VALUES
('A102', 1, 1, 'Somesh DBMS', 'Class Name : CSE E 3 - No.Student : 70'),
('A102', 1, 3, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 1, 4, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 1, 7, 'Somesh PRP', 'Class Name : CSE D - No.Student : 23'),
('A102', 2, 2, 'Raghavan DAA', 'Class Name : CSE D - No.Student : 23'),
('A102', 2, 5, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 3, 3, 'Raghavan OPPS', 'Class Name : CSE D - No.Student : 23'),
('A102', 3, 4, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 3, 5, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 3, 6, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 4, 5, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 4, 7, 'Raghavan DAA', 'Class Name : CSE D - No.Student : 23'),
('A102', 5, 2, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 5, 5, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A102', 6, 5, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A201', 1, 1, 'Raghavan DAA', 'Class Name : CSE E 3 - No.Student : 70'),
('A213', 1, 2, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78'),
('A213', 2, 3, 'Raghavan DAA', 'Class Name : CSE D - No.Student : 23'),
('A213', 5, 4, 'Raghavan DAA', 'Class Name : CSE F - No.Student : 78');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`username`, `password`) VALUES
('rec', 'rec');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `listofclass`
--
ALTER TABLE `listofclass`
  ADD PRIMARY KEY (`Class_id`);

--
-- Indexes for table `professors`
--
ALTER TABLE `professors`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `professor_subjects`
--
ALTER TABLE `professor_subjects`
  ADD KEY `professor_id` (`professor_id`);

--
-- Indexes for table `roomlist`
--
ALTER TABLE `roomlist`
  ADD PRIMARY KEY (`Room_id`);

--
-- Indexes for table `timetable`
--
ALTER TABLE `timetable`
  ADD PRIMARY KEY (`room`,`day`,`period`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `listofclass`
--
ALTER TABLE `listofclass`
  MODIFY `Class_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `professors`
--
ALTER TABLE `professors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `roomlist`
--
ALTER TABLE `roomlist`
  MODIFY `Room_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `professor_subjects`
--
ALTER TABLE `professor_subjects`
  ADD CONSTRAINT `professor_subjects_ibfk_1` FOREIGN KEY (`professor_id`) REFERENCES `professors` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
