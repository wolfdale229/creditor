-- :name new-item! :! :n
-- :docs create a new item inventory.
INSERT INTO creditor (description, credit, debit)
VALUES(:description, :credit, :debit)

-- :name items :? :*
-- :docs display stored inventory items
SELECT * FROM creditor
ORDER BY id

-- :name delete-item :! :n
-- :doc delete item using its id key
DELETE FROM creditor
WHERE id = :id

-- :name update-item :! :n
-- :doc update an inventory item
UPDATE creditor
SET description = :description, credit = :credit, debit = :debit
WHERE id = :id


