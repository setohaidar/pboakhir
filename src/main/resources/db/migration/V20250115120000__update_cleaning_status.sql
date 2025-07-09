-- Update existing cleaning status from code 2 to code 3
-- Sebelumnya: code 2 = "tidak dikonfirmasi" 
-- Sekarang: code 3 = "sudah dibersihkan"

-- Update reservasi yang sudah selesai dan perlu status cleaning yang benar
-- Asumsi: reservasi dengan confirmation = 2 dan use_end < NOW() adalah yang sudah dibersihkan
UPDATE Reservations 
SET confirmation = 3 
WHERE confirmation = 2 
  AND use_end < NOW();

-- Add comment untuk dokumentasi
INSERT INTO migration_log (migration_name, description, executed_at) 
VALUES (
  'V20250115120000__update_cleaning_status.sql',
  'Update cleaning status code from 2 to 3 for completed cleaning tasks',
  NOW()
) ON DUPLICATE KEY UPDATE executed_at = NOW();