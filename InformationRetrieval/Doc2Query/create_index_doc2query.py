# ------------------------------------ SETUP ------------------------------------------------

from tira.third_party_integrations import ensure_pyterrier_is_loaded, persist_and_normalize_run
from tira.rest_api_client import Client

ensure_pyterrier_is_loaded()
tira = Client()

from pyterrier import get_dataset, IterDictIndexer, BatchRetrieve
import pyterrier_doc2query
import pickle
import torch
import os

# ------------------------------------ DATA ------------------------------------------------

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-deep-learning-20241201-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-deep-learning-20241201-training-inforet2024_25"
# attempt_name = "bm25-base-subsampled-ms-marco-deep-learning-20241201-training-inforet2024_25"

dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-rag-20250105-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-rag-20250105-training-inforet2024_25"
attempt_name = "bm25-base-subsampled-ms-marco-rag-20250105-training-inforet2024_25"

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-ir-lab-20250105-test"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-ir-lab-20250105-test-inforet2024_25"
# attempt_name = "bm25-base-subsampled-ms-marco-ir-lab-20250105-test-inforet2024_25"


# needed directory path
store_path = "./content/data/runs"
# check if the directory exists
if not os.path.exists(store_path):
    os.makedirs(store_path)
    print(f"Directory '{store_path}' created.")
else:
    print(f"Directory '{store_path}' already exists.")

# define index path
index_path = "./data/index"
# check if the folder exists
if os.path.exists(index_path):
    # check if the folder is empty
    if os.listdir(index_path):
        print(f"The folder '{index_path}' is not empty. Please delete content first.")
        exit()

# ------------------------------------ CODE ------------------------------------------------

pt_dataset = get_dataset(dataset)

# check for gpu support
device = "cuda" if torch.cuda.is_available() else "cpu"
print(device)

# append generated queries to the orignal document text
print("Create Doc2Query...")
doc2query = pyterrier_doc2query.Doc2Query(append=True, verbose=True, device=device, batch_size=128)

meta = {
    'docno': 50,  # set this to at least 43 or higher to avoid truncation
    'text': 1000000  # adjusted value for text length
}

print("Create IterDictIndexer...")
indexer = doc2query >> IterDictIndexer("./data/index", meta=meta)

print("Create Index...")
index = indexer.index(pt_dataset.get_corpus_iter())

print("Create Batch Retriever...")
bm25 = BatchRetrieve(index, wmodel="BM25", verbose=True)

print("Create Run...")
run = bm25(pt_dataset.get_topics('text'))

# save the 'run' object to a file
try:
    print("Save to file...")
    with open(f"./content/data/{attempt_name}.pkl", 'wb') as f:
        pickle.dump(run, f)
    print("File saved successfully.")
except Exception as e:
    print(f"Failed to save the file: {e}")
    pass

print("Done.")